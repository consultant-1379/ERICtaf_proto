package com.ericsson.cifwk.model;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

public class Dependency implements SerializableToCsv {

    private static final AtomicInteger idGenerator = new AtomicInteger();
    private final String id = String.valueOf(idGenerator.incrementAndGet());

    private final String groupId;
    private final String artifactId;
    private final String version;

    private final String packaging;
    private final String scope;

    public Dependency(String groupId, String artifactId, String version,
                      String packaging, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;

        this.packaging = packaging;
        this.scope = scope;
    }

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public boolean is3pp() {
        return !isEricsson();
    }

    public boolean isEricsson() {
        return groupId.startsWith("com.ericsson");
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getPackaging() {
        return packaging;
    }

    public String getScope() {
        return scope;
    }

    public boolean isProductionScope() {
        return !isTestScope();
    }

    public boolean isTestScope() {
        return scope.equalsIgnoreCase("test");
    }

    public String toCsvRow() {
        StringJoiner row = new StringJoiner(",");
        row.add(quotes(id));
        row.add(quotes(groupId));
        row.add(quotes(artifactId));
        row.add(quotes(version));
        row.add(quotes(packaging));
        row.add(quotes(scope));
        return row.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version) &&
                Objects.equals(packaging, that.packaging) &&
                Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, packaging, scope);
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "id=" + id +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", packaging='" + packaging + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
