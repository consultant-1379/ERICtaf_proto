package com.ericsson.cifwk.taf.grid.client;

import com.google.common.base.Throwables;
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

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class MavenDependencyResolver {

    final File repo;
    private String url;

    public MavenDependencyResolver(String repositoryLocation, String url) {
        this.url = url;
        this.repo = new File(repositoryLocation);
    }

    public Collection<Artifact> fetchArtifact(String groupId, String artifactId, String version) {
        final MavenProject project = new MavenProject();
        project.setRemoteArtifactRepositories(
                Arrays.<ArtifactRepository>asList(
                        new MavenArtifactRepository(
                                "default",
                                url,
                                new DefaultRepositoryLayout(),
                                new ArtifactRepositoryPolicy(),
                                new ArtifactRepositoryPolicy()
                        )
                )
        );

        List<Artifact> artifacts;
        try {
            artifacts = new Aether(project, repo).resolve(
                    new DefaultArtifact(groupId, artifactId, "", "jar", version),
                    JavaScopes.RUNTIME
            );
        } catch (DependencyResolutionException e) {
            throw Throwables.propagate(e);
        }
        return artifacts;
    }

}
