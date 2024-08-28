package com.ericsson.cifwk.application;

import com.ericsson.cifwk.model.Component;
import com.ericsson.cifwk.model.Stako;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public class ComponentParserTest {

    private ComponentParser parser = new ComponentParser();

    @Test
    public void parseSearchPageEntry_happyPath() throws Exception {
        String text = "Guava, Google Ver: 20 PRIM Id: 19/CAX1054600 STAKO: ESW2 PR-Code: PRA DS-Code: DS4 License: Apache License 2.0";

        Optional<Component> optional = parser.parseSearchPageEntry("componentId", text);

        assertThat(optional.isPresent(), is(true));
        Component component = optional.get();
        assertThat(component.getComponentId(), is("componentId"));
        assertThat(component.getName(), is("Guava, Google"));
        assertThat(component.getVersion(), is("20"));
        assertThat(component.getPrimId(), is("19/CAX1054600"));
        assertThat(component.getStako(), is(Stako.GENERAL_USE));
        assertThat(component.getLicense(), is("Apache License 2.0"));
    }

    @Test
    public void parseSearchPageEntry_noLicense() throws Exception {
        String text = "Guava, Google Ver: 20 PRIM Id: 19/CAX1054600 STAKO: ESW2 PR-Code: PRA DS-Code: DS4";

        Optional<Component> optional = parser.parseSearchPageEntry("componentId", text);

        assertThat(optional.isPresent(), is(true));
        Component component = optional.get();
        assertThat(component.getComponentId(), is("componentId"));
        assertThat(component.getName(), is("Guava, Google"));
        assertThat(component.getVersion(), is("20"));
        assertThat(component.getPrimId(), is("19/CAX1054600"));
        assertThat(component.getStako(), is(Stako.GENERAL_USE));
        assertThat(component.getLicense(), is("n/a"));
    }

    @Test
    public void parseSearchPageEntry_noStakoAndLicense() throws Exception {
        String text = "Guava, Google Ver: 20 PRIM Id: 19/CAX1054600";

        Optional<Component> optional = parser.parseSearchPageEntry("componentId", text);

        assertThat(optional.isPresent(), is(true));
        Component component = optional.get();
        assertThat(component.getComponentId(), is("componentId"));
        assertThat(component.getName(), is("Guava, Google"));
        assertThat(component.getVersion(), is("20"));
        assertThat(component.getPrimId(), is("19/CAX1054600"));
        assertThat(component.getStako(), is(Stako.UNKNOWN));
        assertThat(component.getLicense(), is("n/a"));
    }

    @Test
    public void parseComponentPage_happyPath() throws Exception {
        String text = "Guava, Google 20 General information Information status: Checking PRIM status... Category: No categories yet assigned for this component. CPE: PRIM Id: 19/CAX1054600 R-State: R1A PR-Code: PRA DS-Code: DS4 Function Designation: Guava, Google Description: The Guava project contains several of Google's core libraries that we rely on in our Java-based projects: collections, caching, primitives support, concurrency libraries, common annotations, string processing, I/O, and so forth. Version: 20 Release Date: 2016/10 Programming Language: Java License:   FAL1159004/20 ( Apache License 2.0 )    View license obligations. STAKO Classification: ESW2 General Use. ( Component meets the criteria for unrestricted use but it does not meet the criteria defined for a preferred component. ) URL:  Guava, Google Source Code:   Download source code from GASK. Related Documents:   View all 1550's,  View all 2400's,  View latest 1550,  View latest 2400  (Click here if Gask request for latest 2400 fails) Please copy 2400 document number form this page to below input field Document Number: e.g. (2400-CAX 105 XXXX) Export control information US ECCN Code: 5D002C1 ( SW has strong encryption >56 bit symmetric or >=512 asymmetric/hash ) EU ECCN Code: 5D002C1 ( SW has strong encryption >56 bit symmetric or >=512 asymmetric/hash ) BIS Authorization: Include US content Symmetric algorithms: HMAC-MD5 (512)   HMAC   Asymmetric algorithms: Rabin   Hash algorithms: MD5   SHA1   SHA2   Tags Add Tag audit information Guava, Google 20   Discussion Thread. Leave your comments and issues with Guava, Google 20.    My SVLs Added By SVL 1  Guava, Google R08 2  Guava, Google 19.0 3  Guava, Google 18.0 4  Guava, Google 18.0 5  Guava, Google 17.0 6  Guava, Google 17.0 7  Guava, Google 16.0.1 8  Guava, Google 16.0.1 9  Guava, Google 15.0 10  Guava, Google 15.0 11  Guava, Google 14.0.1 12  Guava, Google 14.0.1 13  Guava, Google 14.0.1 14  Guava, Google 13.0.1 15  Guava, Google 12.0.1 16  Guava, Google 12.0.0 17  Guava, Google 11.0.2 18  Guava, Google 11.0.1 19  Guava, Google 10.0.1 20  Guava, Google 10.0-B19 21  Guava, Google 09 22  Guava, Google 0.7";

        Optional<Component> optional = parser.parseComponentPage("componentId", text);

        assertThat(optional.isPresent(), is(true));
        Component component = optional.get();
        assertThat(component.getComponentId(), is("componentId"));
        assertThat(component.getName(), is("Guava, Google 20"));
        assertThat(component.getVersion(), is("20"));
        assertThat(component.getPrimId(), is("19/CAX1054600"));
        assertThat(component.getStako(), is(Stako.GENERAL_USE));
        assertThat(component.getLicense(), is("Apache License 2.0"));
    }
}