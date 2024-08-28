package com.ericsson.cifwk.model;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class DependencyTest {

    @Test
    public void equals_differentIds() throws Exception {
        Dependency dependency1 = new Dependency("groupId", "artifactId", "version", "packaging", "scope");
        Dependency dependency2 = new Dependency("groupId", "artifactId", "version", "packaging", "scope");

        assertThat(dependency1.getId(), not(is(dependency2.getId())));
        assertThat(dependency1, is(dependency2));
    }

    @Test
    public void equals_differentFields() throws Exception {
        Dependency dependency1 = new Dependency("groupId", "artifactId", "version", "packaging", "scope");
        Dependency dependency2 = new Dependency("foo", "artifactId", "version", "packaging", "scope");
        Dependency dependency3 = new Dependency("groupId", "foo", "version", "packaging", "scope");
        Dependency dependency4 = new Dependency("groupId", "artifactId", "foo", "packaging", "scope");
        Dependency dependency5 = new Dependency("groupId", "artifactId", "version", "foo", "scope");
        Dependency dependency6 = new Dependency("groupId", "artifactId", "version", "packaging", "foo");

        List<Dependency> dependencyList = asList(dependency1, dependency2, dependency3, dependency4, dependency5, dependency6);
        Set<Dependency> dependencySet = new HashSet<>(dependencyList);

        assertThat(dependencyList.size(), is(dependencySet.size()));
    }
}