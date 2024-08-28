package com.ericsson.cifwk.application.print;

import com.ericsson.cifwk.model.Component;
import com.ericsson.cifwk.model.Dependency;
import com.ericsson.cifwk.model.Stako;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class DependencyPrinterTest {

    private DependencyPrinter printer = new DependencyPrinter("");

    @Test
    public void dependencyCsvWithHyperlink() throws Exception {
        Dependency dependency = new Dependency("groupId", "artifactId", "version", "packaging", "scope");

        String hyperlink = printer.dependencyCsvWithHyperlink(dependency);

        assertThat(hyperlink, containsString("\"groupId\",\"=HYPERLINK(\"\"https://bazaar.internal.ericsson.com" +
                "/b-view-main-ajax-getsearchresults.php?search=artifactId&numppage=50&page=1\"\", \"\"artifactId\"\")\",\"version\","));
    }

    @Test
    public void componentCsvWithHyperlink() throws Exception {
        Component component = new Component("42", "name", "version", "primId", Stako.PREFERRED, "license");

        String hyperlink = printer.componentCsvWithHyperlink(component);

        assertThat(hyperlink, startsWith("\"=HYPERLINK(\"\"https://bazaar.internal.ericsson.com/b-view-component.php?componentid=42\"\", \"\"42\"\")\",\"name\","));
    }
}