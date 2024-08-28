package com.ericsson.cifwk.taf.ui.acceptance.test.cases;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.acceptance.test.operators.TafUiOperator;
import com.ericsson.cifwk.taf.ui.acceptance.test.operators.TafUiOperatorImpl;
import com.ericsson.cifwk.taf.ui.acceptance.test.operators.TestHelper;
import com.ericsson.cifwk.taf.ui.acceptance.test.viewmodels.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

class AbstractTafUiAcceptanceTestCase extends TorTestCaseHelper implements TestCase {
	private Server jettyServer;
	
	@BeforeClass
	public void setUp() {
		jettyServer = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(TestHelper.getJettyPort());
		jettyServer.addConnector(connector);

		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);

		resourceHandler.setResourceBase("target/classes/mock_pages/");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, new DefaultHandler() });
		jettyServer.setHandler(handlers);

		try {
			jettyServer.start();
			//jettyServer.join(); // Sometimes hangs up
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@AfterClass
	public void tearDown() {
		try {
			jettyServer.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected BasicComponentsView openComponentsView(BrowserType browserType) {
		TafUiOperator operator = getOperator(browserType);
		return operator.openComponentsView();
	}

	protected ViewModel openGenericView(BrowserType browserType, String page) {
		BrowserTab browserTab = openBrowserTab(browserType, page);
		return browserTab.getGenericView();
	}

	protected BrowserTab openBrowserTab(BrowserType browserType, String page) {
		TafUiOperator operator = getOperator(browserType);
		BrowserTab browserTab = operator.openBrowserTab(page);
		browserTab.maximize();
		return browserTab;
	}

	protected TafUiOperator getOperator(BrowserType browserType) {
		return new TafUiOperatorImpl(browserType);
	}
}
