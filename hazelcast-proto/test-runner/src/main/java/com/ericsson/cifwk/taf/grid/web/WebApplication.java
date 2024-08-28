package com.ericsson.cifwk.taf.grid.web;

import com.ericsson.cifwk.taf.grid.Configurations;
import com.ericsson.cifwk.taf.grid.client.*;
import com.ericsson.cifwk.taf.grid.shared.TestSchedule;
import com.ericsson.cifwk.taf.grid.shared.TestSpecification;
import com.ericsson.cifwk.taf.grid.shared.TestStep;
import com.ericsson.cifwk.taf.performance.metric.Metrics;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.configuration.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import javax.servlet.ServletOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 *
 */
public final class WebApplication implements SparkApplication {

    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);

    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_STREAM = "application/octet-stream";

    private final Configuration configuration;

    public WebApplication() {
        this(loadConfiguration());
    }

    public WebApplication(Configuration configuration) {
        this.configuration = configuration;
    }

    private static CompositeConfiguration loadConfiguration() {
        String env = System.getProperty(Configurations.ENVIRONMENT, "default");
        CompositeConfiguration config = new CompositeConfiguration();
        config.addConfiguration(new SystemConfiguration());
        try {
            config.addConfiguration(new PropertiesConfiguration(env + ".properties"));
        } catch (ConfigurationException e) {
            throw Throwables.propagate(e);
        }
        return config;
    }

    @Override
    public void init() {
        startServer();
    }

    public String address() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw Throwables.propagate(e);
        }
    }

    public DistributedTestRunner startServer() {
        final String repoLocation = System.getProperty("user.home") + "/.m2/repository/";

        if ("dev".equals(configuration.getString(Configurations.ENVIRONMENT))) {
            final int port = configuration.getInt(Configurations.HTTP_PORT);
            setPort(port);

            externalStaticFileLocation("./hazelcast-proto/client");
        }

        final MetricsService metricsService = new MetricsService();
        final ClusterClient clusterClient = ClusterClientFactory.createClient(configuration, metricsService);
        final DistributedTestRunner runner = new DistributedTestRunner(clusterClient);
        logger.info("Binding to IP address {0}", address());
        clusterClient.connect();

        final Gson gson = createGson();

        get(new Route("/api") {
            @Override
            public Object handle(Request request, Response response) {
                response.type(APPLICATION_JSON);
                return gson.toJson("Hello World!");
            }
        });

        post(new Route("/api/test") {
            @Override
            public Object handle(Request request, Response response) {
                String contextPath = request.contextPath();

                response.type(APPLICATION_JSON);
                TestDto dto = gson.fromJson(request.body(), TestDto.class);
                TestSpecification specification = map(dto);

                String fullClasspath = ClasspathBuilder.buildClasspath(configuration,
                        specification.getTestware(),
                        contextPath,
                        address());
                specification.setTestware(fullClasspath);

                runner.run(specification);
                return gson.toJson("OK");
            }
        });

        get(new Route("/api/cluster") {
            @Override
            public Object handle(Request request, Response response) {
                response.type(APPLICATION_JSON);

                List<String> topology = clusterClient.topology();
                List<ServerDto> result = Lists.newArrayList();
                for (String member : topology) {
                    ServerDto serverDto = new ServerDto(member);
                    result.add(serverDto);
                }

                return gson.toJson(result);
            }
        });

        get(new Route("/api/metrics") {
            @Override
            public Object handle(Request request, Response response) {
                response.type(APPLICATION_JSON);
                Metrics metrics = metricsService.getMetrics();
                MetricsDto dto = new MetricsDto(metrics);
                return gson.toJson(dto);
            }
        });

        get(new Route("/api/jar/:groupId/:artifactId/:version/.jar") {
            @Override
            public Object handle(Request request, Response response) {
                response.type(APPLICATION_STREAM);
                String groupId = request.params("groupId");
                String artifactId = request.params("artifactId");
                String version = request.params("version");

                try (ServletOutputStream outputStream = response.raw().getOutputStream()) {
                    String filename = artifactId + "-" + version + ".jar";
                    String location = repoLocation + groupId.replaceAll("\\.", "/")
                            + "/" + artifactId + "/" + version + "/" + filename;
                    logger.info("streaming maven artifact : {}", location);

                    response.header("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
                    FileInputStream inputStream = new FileInputStream(location);
                    ByteStreams.copy(inputStream, outputStream);
                    inputStream.close();
                    response.status(200);
                } catch (IOException e) {
                    response.status(404);
                }
                return null;
            }
        });
        return runner;
    }

    private static TestSpecification map(TestDto dto) {
        TestSpecification specification = new TestSpecification();
        specification.setTestware(dto.testware);
        specification.setRunner(dto.runner);

        TestStep[] testSteps = new TestStep[dto.testSteps.length];
        TestDto.Step[] testStepDtos = dto.testSteps;
        for (int i = 0; i < testStepDtos.length; i++) {
            TestDto.Step step = testStepDtos[i];
            TestStep testStep = new TestStep();
            Map<String, String> attributes = new HashMap<>();
            attributes.put(step.key1, step.value1);
            attributes.put(step.key2, step.value2);
            attributes.put(step.key3, step.value3);
            testStep.setAttributes(attributes);

            long fromTime;
            if (step.from != null) {
                fromTime = step.from.getTime();
            } else {
                fromTime = Long.MIN_VALUE;
            }

            long untilTime;
            if (step.from != null) {
                untilTime = step.until.getTime();
            } else {
                untilTime = Long.MAX_VALUE;
            }

            testStep.setSchedule(new TestSchedule(step.repeatCount, fromTime, untilTime, step.vUsers));
            testSteps[i] = testStep;
        }
        specification.setTestSteps(testSteps);
        return specification;
    }

    private Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeSpecialFloatingPointValues();
        gsonBuilder.setDateFormat("yyyy/mm/dd hh:mm:ss");
        return gsonBuilder.create();
    }

}
