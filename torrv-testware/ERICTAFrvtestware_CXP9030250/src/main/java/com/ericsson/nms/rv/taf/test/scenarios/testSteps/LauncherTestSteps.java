package com.ericsson.nms.rv.taf.test.scenarios.testSteps;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.nms.rv.taf.test.scenarios.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.cifwk.taf.TestContext;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.nms.launcher.LauncherOperator;

@Operator
public class LauncherTestSteps {
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";

    private final Logger logger = LoggerFactory
            .getLogger(LauncherTestSteps.class);

    @Inject
    TestContext context;

    @Inject
    LauncherOperator launcher;

    @TestStep(id = LOGIN)
    public void login(@Input("user") User user) {
        logger.info("login : {}", user);
        HttpTool tool = launcher.login(user);
        UserSession session = new UserSession(tool);
        context.setAttribute("session", session);
    }

    @TestStep(id = LOGOUT)
    public void logout() {
        logger.info("logout");
        launcher.logout();
        context.getAttribute("session");
    }
}
