package com.ericsson.nms.rv.taf.test.cmapache.cases;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.DataProviders;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.datasource.DataRecord;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.nms.launcher.LauncherOperator;
import com.ericsson.nms.node.NetworkNodeOperator;
import com.ericsson.nms.node.NetworkNodeRestOperator;
import com.ericsson.nms.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import javax.inject.Inject;

/**
 * Tests Add node test case
 *
 */
public class NodeTest extends TorTestCaseHelper implements TestCase {

    Logger log = LoggerFactory.getLogger(NodeTest.class);

    @Inject NetworkNodeRestOperator node;

    @Inject
    private OperatorRegistry registry;

    @Inject
    TestContext context;

    @DataProviders({@DataDriven(name="node"),@DataDriven(name="user")})
    @Test
    @Context(context = { Context.REST })
    public void verifyAddingNodeBehavesAsExpected(@Input("user.user") User user,
            @Input("node.node") Node nodeToBeAdded,
            @Output("node.outputOfNodeAdding") boolean nodeIsAdded) {
        log.info("User {} and node {} are being provided in vuser {}", user, nodeToBeAdded,context.getVUser());
        LauncherOperator launcherOperator = (LauncherOperator) registry.provide(LauncherOperator.class);
        HttpTool tool = launcherOperator.login(user);

        node.setTool(tool);
        //assertTrue(node.add(nodeToBeAdded));
        context.dataSource("addedNode").addRecord().setField("node",nodeToBeAdded);
    }

    @DataDriven(name = "addedNode")
    @Test
    @Context(context = { Context.REST })
    public void deleteNode(@Input("addedNode.node") Node nodeToDelete){
        node.delete(nodeToDelete.getName());
        assertFalse(node.isNodeAdded(nodeToDelete));
    }

}