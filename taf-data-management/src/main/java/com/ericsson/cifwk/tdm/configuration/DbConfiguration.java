package com.ericsson.cifwk.tdm.configuration;

import com.github.mongobee.Mongobee;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author Alexey Nikolaenko alexey.nikolaenko@ericsson.com
 *         Date: 17/02/2016
 */
@Configuration
public class DbConfiguration {

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    @Value("${spring.data.mongodb.host}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

    @Value("${spring.data.mongodb.database}")
    private String mongoDatabase;

    @Bean(destroyMethod = "stop")
    public MongodProcess mongod() throws IOException {
        return mongodExe().start();
    }

    @Bean(destroyMethod = "stop")
    public MongodExecutable mongodExe() throws IOException {
        return starter.prepare(mongodConfig());
    }

    @Bean
    public IMongodConfig mongodConfig() throws IOException {
        return new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(mongoPort, Network.localhostIsIPv6()))
                .build();
    }

    @Bean
    public Jongo jongoProvider() throws UnknownHostException {
        MongoClient mongo = new MongoClient(mongoHost, mongoPort);
        DB db = mongo.getDB(mongoDatabase);
        return new Jongo(db);
    }

    @Bean
    public Mongobee mongobee() {
        String mongoURI = String.format("mongodb://%s:%d/%s", mongoHost, mongoPort, mongoDatabase);
        Mongobee runner = new Mongobee(mongoURI);
        runner.setChangeLogsScanPackage(
                "com.ericsson.cifwk.tdm.configuration.changelogs");
        return runner;
    }
}
