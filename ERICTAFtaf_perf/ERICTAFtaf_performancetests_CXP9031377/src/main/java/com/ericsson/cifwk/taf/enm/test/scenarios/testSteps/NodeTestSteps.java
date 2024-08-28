package com.ericsson.cifwk.taf.enm.test.scenarios.testSteps;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.nms.node.NetworkNodeRestOperator;
import com.ericsson.nms.node.Node;
import com.ericsson.cifwk.taf.enm.test.scenarios.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static com.ericsson.cifwk.taf.assertions.TafAsserts.assertFalse;
import static com.ericsson.cifwk.taf.assertions.TafAsserts.assertTrue;

@Operator
public class NodeTestSteps {
    public static final String ADD_NODE = "addNode";
    public static final String DELETE_NODE = "deleteNode";
    public static final String SET_NODE_ATTRIBUTE = "setNodeAttribute";
    public static final String CREATE_SUBSCRIPTION = "createSubscription";
    public static final String DELETE_SUBSCRIPTION = "deleteSubscription";
    public static final String CREATE_COLLECTION = "createCollection";
    public static final String DELETE_COLLECTION = "deleteCollection";
    private final Logger logger = LoggerFactory.getLogger(NodeTestSteps.class);

    @Inject
    TestContext context;

    @Inject
    NetworkNodeRestOperator nodeRestOperator;

    @TestStep(id = ADD_NODE)
    public void addNode(@Input("node") Node node) {
        logger.info("addNode : {}", node.getName());
        nodeRestOperator.setTool(((UserSession) context.getAttribute("session")).getTool());
        assertTrue(nodeRestOperator.add(node));
        context.dataSource("addedNode").addRecord().setField("node",node);
    }

    @TestStep(id = CREATE_SUBSCRIPTION)
    public void createSubscription(@Input("Nodes") String node,
            @Input("subscriptionName") String subscription,
            @Input("Description") String description) {
        logger.info("createSubscription : {}", subscription);
        context.getAttribute("session");
        context.dataSource("AddedSubscriptions").addRecord().setField("subscriptionName",subscription);
    }

    @TestStep(id = DELETE_SUBSCRIPTION)
    public void deleteSubscription(@Input("AddedSubscriptions.subscriptionName") String subscription) {
        logger.info("deleteSubscription : {}", subscription);
        context.getAttribute("session");
    }

    @TestStep(id = DELETE_NODE)
    public void deleteNode(@Input("addedNode.node") Node node) {
        logger.info("deleteNode : {}", node.getName());
        nodeRestOperator.delete(node.getName());
        assertFalse(nodeRestOperator.isNodeAdded(node));
    }

    @TestStep(id = SET_NODE_ATTRIBUTE)
    public void setNodeAttribute(@Input("nodeAttributes.node") String node,
            @Input("attribute") String attribute, @Input("value") String value) {
        logger.info("setNodeAttribute : {} => {}", attribute, value);
        context.getAttribute("session");
    }

    @TestStep(id = CREATE_COLLECTION)
    public void createCollection(@Input("managedElementIdList") String node,
            @Input("collectionName") String collection) {
        logger.info("createCollection : {}", collection);
        context.getAttribute("session");
        context.dataSource("addedCollections").addRecord().setField("collectionName",collection);
    }

    @TestStep(id = DELETE_COLLECTION)
    public void deleteCollection(@Input("addedCollections.collectionName") String collection) {
        logger.info("deleteCollection : {}", collection);
        context.getAttribute("session");
    }
}
