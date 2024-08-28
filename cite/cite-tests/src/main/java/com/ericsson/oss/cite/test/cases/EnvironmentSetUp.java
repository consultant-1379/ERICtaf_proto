package com.ericsson.oss.cite.test.cases;

import javax.inject.Inject;

import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.oss.cite.operators.EnvironmentSetUpOperator;

public class EnvironmentSetUp extends TorTestCaseHelper implements TestCase {

    @Inject
    OperatorRegistry<EnvironmentSetUpOperator> operatorRegistry;

    private EnvironmentSetUpOperator getOperator() {
        return operatorRegistry.provide(EnvironmentSetUpOperator.class);
    }

    @Context(context = { Context.CLI })
    @Test
    public void verifyEnvironmentInstallation() {
        assertTrue(getOperator().executeInitialInstall());
    }

    @Context(context = { Context.CLI })
    @Test(dependsOnMethods = { "verifyEnvironmentInstallation" })
    public void verifyNetworkPreparation() {
        assertTrue(getOperator().executeArneImport());
    }

    @Context(context = { Context.CLI })
    @Test(dependsOnMethods = { "verifyNetworkPreparation" })
    public void verifyUsersPreparation() {
        assertTrue(getOperator().prepareUsers());
    }
}
