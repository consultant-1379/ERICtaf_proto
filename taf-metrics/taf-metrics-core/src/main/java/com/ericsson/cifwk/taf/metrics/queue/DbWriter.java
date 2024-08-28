package com.ericsson.cifwk.taf.metrics.queue;

import com.ericsson.cifwk.taf.metrics.sample.Dumpling;
import com.ericsson.cifwk.taf.metrics.sample.Sample;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static com.ericsson.cifwk.taf.metrics.beans.Tables.*;
import static org.jooq.impl.DSL.select;

public class DbWriter implements Dumpling {

    private static final String SQL_BOOTSTRAP = "com/ericsson/cifwk/taf/metrics/sample/bootstrap.sql";

    private final Connection conn;

    public DbWriter(Connection conn) {
        this.conn = conn;
    }

    public void initialize() throws IOException {
        URL bootstrapResource = Resources.getResource(SQL_BOOTSTRAP);
        String bootstrap = Resources.toString(bootstrapResource, Charsets.UTF_8);

        try {
            String[] stmts = bootstrap.split(";");
            for (String stmt : stmts) {
                if (!stmt.trim().isEmpty()) {
                    conn.createStatement().executeUpdate(stmt);
                }
            }
        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }
    }

    public void write(Sample sample) throws IOException {
        DSLContext context = DSL.using(conn, SQLDialect.MYSQL);
        context.insertInto(EXECUTIONS)
                .set(EXECUTIONS.NAME, sample.getExecutionId())
                .onDuplicateKeyIgnore()
                .execute();

        context.insertInto(TEST_SUITES)
                .set(TEST_SUITES.NAME, sample.getTestSuite())
                .set(TEST_SUITES.EXECUTION_ID,
                        select(EXECUTIONS.ID)
                                .from(EXECUTIONS)
                                .where(EXECUTIONS.NAME.eq(sample.getExecutionId()))
                )
                .onDuplicateKeyIgnore()
                .execute();

        context.insertInto(TEST_CASES)
                .set(TEST_CASES.NAME, sample.getTestCase())
                .set(TEST_CASES.TEST_SUITE_ID,
                        select(TEST_SUITES.ID)
                                .from(TEST_SUITES)
                                .where(TEST_SUITES.NAME.eq(sample.getTestSuite()))
                )
                .onDuplicateKeyIgnore()
                .execute();

        Date eventTime = sample.getEventTime();
        Calendar minute = Calendar.getInstance();
        minute.setTime(eventTime);
        minute.set(Calendar.MILLISECOND, 0);
        minute.set(Calendar.SECOND, 0);

        Calendar date = Calendar.getInstance();
        date.setTime(eventTime);
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR, 0);

        Timestamp timeId = new Timestamp(minute.getTime().getTime());
        context.insertInto(TIME)
                .set(TIME.ID, timeId)
                .set(TIME.DAY, new java.sql.Date(date.getTime().getTime()))
                .set(TIME.HOUR, minute.get(Calendar.HOUR_OF_DAY))
                .set(TIME.MINUTE, minute.get(Calendar.MINUTE))
                .onDuplicateKeyIgnore()
                .execute();

        context.insertInto(SAMPLES)
                .set(SAMPLES.THREAD_ID, sample.getThreadId())
                .set(SAMPLES.VUSER_ID, sample.getVuserId())
                .set(SAMPLES.TEST_CASE_ID,
                        select(TEST_CASES.ID)
                                .from(TEST_CASES)
                                .where(TEST_CASES.NAME.eq(sample.getTestCase()))
                )
                .set(SAMPLES.TIME_ID, timeId)
                .set(SAMPLES.PROTOCOL, sample.getProtocol())
                .set(SAMPLES.TARGET, sample.getTarget().toString())
                .set(SAMPLES.REQUEST_TYPE, sample.getRequestType())
                .set(SAMPLES.REQUEST_SIZE, sample.getRequestSize())
                .set(SAMPLES.RESPONSE_CODE, 0) // TODO Add response codes to samples
                .set(SAMPLES.SUCCESS, (byte) (sample.isSuccess() ? 1 : 0))
                .set(SAMPLES.RESPONSE_TIME, sample.getResponseTime())
                .set(SAMPLES.LATENCY, sample.getLatency())
                .set(SAMPLES.RESPONSE_SIZE, sample.getResponseSize())
                .execute();
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }
}
