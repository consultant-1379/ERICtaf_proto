package com.ericsson.cifwk.taf.metrics.queue;

import com.ericsson.cifwk.taf.metrics.sample.Sample;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AmqpConsumer {

    private final Logger logger = LoggerFactory.getLogger(AmqpConsumer.class);

    private final Kryo kryo;
    private final AmqpClient amqp;
    private final DbWriter dbWriter;

    public AmqpConsumer(Kryo kryo,
                        AmqpClient amqp,
                        DbWriter dbWriter) {
        this.kryo = kryo;
        this.amqp = amqp;
        this.dbWriter = dbWriter;

    }

    public void start() throws IOException {
        dbWriter.initialize();
        amqp.connect();
        amqp.subscribe(new BodyConsumer() {
            @Override
            public void handle(byte[] body) {
                consumeSample(body);
            }
        });
    }

    private void consumeSample(byte[] body) {
        try {
            final Sample sample = kryo.readObject(new Input(body), Sample.class);
            sample.validate();
            dbWriter.write(sample);
        } catch (Exception e) {
            logger.error("Error consuming sample from queue", e);
        }
    }

    public void shutdown() throws IOException {
        amqp.shutdown();
        dbWriter.close();
    }
}
