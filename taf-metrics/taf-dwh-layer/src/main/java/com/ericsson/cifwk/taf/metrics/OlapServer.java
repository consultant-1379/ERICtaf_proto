package com.ericsson.cifwk.taf.metrics;

import com.google.common.base.Throwables;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class OlapServer {

    OlapConnection olapConnection;

    public void connect() {
        try {
            Configuration configuration = loadConfiguration();
            olapConnection = createConnection(configuration);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public OlapConnection getOlapConnection() {
        return olapConnection;
    }

    public CellSet mdx(String query) {
        try {
            return olapConnection.createStatement().executeOlapQuery(query);
        } catch (OlapException e) {
            throw Throwables.propagate(e);
        }
    }

    public void disconnect() {
        if (olapConnection != null) {
            try {
                olapConnection.close();
            } catch (SQLException e) {
                throw Throwables.propagate(e);
            }
        }
    }

    private OlapConnection createConnection(Configuration config) throws Exception {
        Class.forName("mondrian.olap4j.MondrianOlap4jDriver");

        String dbUrl = config.getString("conn.db.url");

        final Connection connection =
                DriverManager.getConnection(
                        "jdbc:mondrian:"
                                + "Jdbc=" + dbUrl + ";"
                                + "Catalog=res:samples.xml;"
                                + "JdbcDrivers=com.mysql.jdbc.Driver"
                );

        return connection.unwrap(OlapConnection.class);
    }

    private Configuration loadConfiguration() throws Exception {
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        config.addConfiguration(new PropertiesConfiguration("connection.properties"));
        return config;
    }

}
