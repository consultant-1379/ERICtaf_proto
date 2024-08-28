package com.ericsson.cifwk.taf.grid;

import com.jcabi.aether.Aether;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.project.MavenProject;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.context.DefaultContextLoader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 */
public class AetherTest {

    public static void main(String[] args) throws Exception {
        Collection<Artifact> deps = fetchDependencies();
        loadClasspath(deps);
    }

    private static Collection<Artifact> fetchDependencies() throws DependencyResolutionException {
        final MavenProject project = new MavenProject();
        project.setRemoteArtifactRepositories(
                Arrays.<ArtifactRepository>asList(
                        new MavenArtifactRepository(
                                "ericsson",
                                "https://arm1s11-eiffel004.eiffel.gic.ericsson.se:8443/nexus/content/groups/public",
                                new DefaultRepositoryLayout(),
                                new ArtifactRepositoryPolicy(),
                                new ArtifactRepositoryPolicy()
                        )
                )
        );

        final File repo = new File(System.getProperty("user.home") + "/.m2/repository/");
        return new Aether(project, repo).resolve(
                new DefaultArtifact("com.ericsson.cifwk.taf", "testware", "", "jar", "1.0.0-SNAPSHOT"),
                JavaScopes.RUNTIME
        );
    }

    private static void loadClasspath(Collection<Artifact> deps) throws Exception {
        JarClassLoader jarClassLoader = new JarClassLoader();
        for (Artifact dep : deps) {
            jarClassLoader.add(new FileInputStream(dep.getFile()));
        }

        DefaultContextLoader contextLoader = new DefaultContextLoader(jarClassLoader);
        contextLoader.loadContext();

        Class type = jarClassLoader.loadClass("com.ericsson.cifwk.taf.grid.sample.SampleTestCase");
        System.out.println(type);

        contextLoader.unloadContext();
    }

}
