package com.ericsson.cifwk.taf.grid.client;

import com.ericsson.cifwk.taf.grid.Configurations;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import org.apache.commons.configuration.Configuration;
import org.sonatype.aether.artifact.Artifact;

import java.util.Collection;

public final class ClasspathBuilder {

    private ClasspathBuilder() {
    }

    public static String buildClasspath(final Configuration configuration,
                                        final String testware,
                                        final String contextPath,
                                        final String address) {
        if (Strings.isNullOrEmpty(testware)) {
            return "";
        }

        final int port = configuration.getInt(Configurations.HTTP_PORT);
        final String mavenRepository = configuration.getString(Configurations.MAVEN_REPO);
        final String repoLocation = System.getProperty("user.home") + "/.m2/repository/";
        final MavenDependencyResolver mavenResolver = new MavenDependencyResolver(repoLocation, mavenRepository);

        String[] tokens = testware.split(":");
        Preconditions.checkArgument(tokens.length == 3);
        Collection<Artifact> classpathArtifacts = mavenResolver.fetchArtifact(tokens[0], tokens[1], tokens[2]);
        Iterable<String> classpath = Iterables.transform(classpathArtifacts, new Function<Artifact, String>() {
            @Override
            public String apply(Artifact input) {
                return "http://" + address + ":" + port + "/" + contextPath + "/api/jar/"
                        + input.getGroupId() + "/" + input.getArtifactId() + "/" + input.getVersion() + "/.jar";
            }
        });
        return Joiner.on(";").join(classpath);
    }

}
