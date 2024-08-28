package com.ericsson.cifwk.taf.metrics.queue;

import com.ericsson.cifwk.taf.metrics.sample.Dumpling;
import com.ericsson.cifwk.taf.metrics.sample.Sample;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AmqpDumpling implements Dumpling {

    private final Kryo kryo;
    private final AmqpClient amqp;

    AmqpDumpling(Kryo kryo, AmqpClient amqp) {
        this.kryo = kryo;
        this.amqp = amqp;
    }

    public static AmqpDumpling create(AmqpClient client) {
        return new AmqpDumpling(new Kryo(), client);
    }

    @Override
    public void initialize() throws IOException {
        amqp.connect();
    }

    @Override
    public void write(Sample sample) throws IOException {
        byte[] bytes = serializeSample(sample);
        amqp.send(bytes);
    }

    public byte[] serializeSample(Sample sample) throws IOException {
        sample.validate();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Output output = new Output(outputStream)) {
            kryo.writeObject(output, sample);
            output.flush();
        }
        byte[] bytes = outputStream.toByteArray();
        outputStream.close();
        return bytes;
    }

    @Override
    public void flush() throws IOException {
        // Handled by AMQP client
    }

    @Override
    public void close() throws IOException {
        amqp.shutdown();
    }
}
