package com.ericsson.cifwk.taf.ui.acceptance.test.cases;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.acceptance.test.data.MultiBrowserTestDataProvider;
import com.ericsson.cifwk.taf.ui.acceptance.test.operators.TafUiOperator;
import com.ericsson.cifwk.taf.ui.acceptance.test.viewmodels.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.MessageBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

public class WindowOperationsTest extends AbstractTafUiAcceptanceTestCase {
	
	@BeforeClass
	public void setUp() {
		super.setUp();
	}
	
	@AfterMethod
	public void tearDown() {
		UI.closeAllWindows();
	}
	
	@TestId(id = "CIP-3515-func-getAlert", title = "Verify that it's possible to detect and manage alert windows")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void getAlertWindow(BrowserType browserType) {
		BrowserTab browserTab = openBrowserTab(browserType, "basic_components.htm");
		BasicComponentsView view = browserTab.getView(BasicComponentsView.class);
		
		Button button = view.getButton();
		Assert.assertNull(browserTab.getMessageBox());
		button.click();
		MessageBox messageBox = browserTab.getMessageBox();
		Assert.assertNotNull(messageBox);
		Assert.assertEquals("Button clicked", messageBox.getText());
		messageBox.clickOk();
		Assert.assertNull(browserTab.getMessageBox());
	}

	@TestId(id = "CIP-3515-func-open", title = "Verify that it's possible to open browser window")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void open(BrowserType browserType) {
		TafUiOperator operator = getOperator(browserType);
		Assert.assertEquals(0, operator.getOpenedWindowsAmount());
		BrowserTab browserTab = operator.openBrowserTab("basic_components.htm");
		Assert.assertEquals(1, operator.getOpenedWindowsAmount());
		Assert.assertEquals("Tests of Web elements' wrappers", browserTab.getTitle());
	}

	@TestId(id = "CIP-3515-func-refresh", title = "Verify it's possible to refresh the page")
	@Test
	public void refresh(BrowserType browserType) {
		BrowserTab tab = openBrowserTab(browserType, "basic_components.htm");
		ViewModel view = tab.getGenericView();
		Assert.assertFalse(view.hasComponent("#newLabel"));

		Button button = view.getButton("#divGenButton");
		button.click();

		Assert.assertTrue(view.hasComponent("#newLabel"));
		
		tab.refreshPage();
		Assert.assertFalse(view.hasComponent("#newLabel"));
	}

	@TestId(id = "CIP-3515-func-close", title = "Verify that it's possible to close browser window")
	@Test
	public void close(BrowserType browserType) {
		TafUiOperator operator = getOperator(browserType);
		BrowserTab firstTab = operator.openBrowserTab("sso_login.htm");
		
		Assert.assertEquals(1, operator.getOpenedWindowsAmount());
		
		BrowserTab secondTab = operator.openBrowserTab("basic_components.htm");
		Assert.assertEquals(2, operator.getOpenedWindowsAmount());

		BrowserTab thirdTab = operator.openBrowserTab("sso_applications.htm");
		Assert.assertEquals(3, operator.getOpenedWindowsAmount());

		Browser browser = operator.getBrowser();
		browser.closeTab(secondTab);
		
		Assert.assertEquals(2, operator.getOpenedWindowsAmount());
		Assert.assertEquals(thirdTab.getWindowDescriptor(), browser.getCurrentWindow().getWindowDescriptor());
		
		browser.closeTab(thirdTab);
		
		Assert.assertEquals(1, operator.getOpenedWindowsAmount());
		Assert.assertEquals(firstTab.getWindowDescriptor(), browser.getCurrentWindow().getWindowDescriptor());
	}

	@TestId(id = "CIP-3515-func-windowMaximize", title = "Verify that it's possible to maximize browser window")
	@Test
	public void windowMaximize(BrowserType browserType) {
		BrowserTab browserTab = openBrowserTab(browserType, "basic_components.htm");
		
		UiComponentSize originalSize = browserTab.getSize();
		browserTab.maximize();
		
		UiComponentSize maxSize = browserTab.getSize();
		Assert.assertTrue(originalSize.getWidth() < maxSize.getWidth());
		Assert.assertTrue(originalSize.getHeight() < maxSize.getHeight());
	}
	
	
	@TestId(id = "CIP-3515-func-getEval", title = "Verify it's possible to evaluate a Javascript expression")
	// evaluate() doesn’t work with Firefox 23 and 24
	@Test
	public void getEval(BrowserType browserType) {
		BrowserTab browserTab = openBrowserTab(browserType, "basic_components.htm");
		
		// Returns a timestamp
		String result = (String) browserTab.evaluate("return '' + new Date();");
		Assert.assertFalse(StringUtils.isBlank(result));
	}
}
