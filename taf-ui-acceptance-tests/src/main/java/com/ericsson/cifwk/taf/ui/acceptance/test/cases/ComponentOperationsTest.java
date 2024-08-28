package com.ericsson.cifwk.taf.ui.acceptance.test.cases;

import java.util.List;

import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.acceptance.test.data.MultiBrowserTestDataProvider;
import com.ericsson.cifwk.taf.ui.acceptance.test.viewmodels.BasicComponentsView;
import com.ericsson.cifwk.taf.ui.core.SelectorType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentNotFoundException;
import com.ericsson.cifwk.taf.ui.core.UiComponentSize;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.Label;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

public class ComponentOperationsTest extends AbstractTafUiAcceptanceTestCase {

	@BeforeClass
	public void setUp() {
		super.setUp();
	}
	
	@AfterMethod
	public void tearDown() {
		UI.closeAllWindows();
	}

	@TestId(id = "CIP-3515-func-click", title = "Verify component can be clicked")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void click(BrowserType browserType) {
		BasicComponentsView componentsView = openComponentsView(browserType);
		TextBox timestampBox = componentsView.getTimestampBox();
		Button genButton = componentsView.getTimestampGenerator();
		
		Assert.assertEquals("", timestampBox.getText());
		// Button click fills the text box with current timestamp
		genButton.click();
		Assert.assertNotEquals("", timestampBox.getText());
	}

	@TestId(id = "CIP-3515-func-waitForElementToLoad", title = "Verify it's possible to wait until the page element is loaded")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void waitForElementToLoad(BrowserType browserType) {
		BrowserTab tab = openBrowserTab(browserType, "basic_components.htm");

		long startTime = System.currentTimeMillis();
		// Element #appearingDiv appears in 3 seconds after page is loaded
		UiComponent appearingComponent = tab.waitUntilComponentIsDisplayed("#appearingDiv", 3100);
		// Javascript and system timers can differ, so let's check for a time period, smaller than 3 seconds
		Assert.assertTrue(System.currentTimeMillis() - startTime >= 2500);
		Assert.assertNotNull(appearingComponent);
		
		Assert.assertEquals("commonClass", appearingComponent.getProperty("class"));
		Assert.assertEquals("Text appearing in 3 seconds...", appearingComponent.getText());
	}

	@TestId(id = "CIP-3515-func-type", title = "Verify it's possible to type text in page element")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void type(BrowserType browserType) {
		BasicComponentsView componentsView = openComponentsView(browserType);

		// Simple text entering (typing)
		TextBox textBox = componentsView.getTextBox("#textBoxId");
		textBox.setText("My precious text");
		Assert.assertEquals("My precious text", textBox.getText());

		textBox.sendKeys(Keys.CONTROL + "a");
		textBox.sendKeys(Keys.DELETE);
		Assert.assertEquals("", textBox.getText());
	}

	@TestId(id = "CIP-3515-func-isElementPresent", title = "Verify it's possible to check element presence")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void isElementPresent(BrowserType browserType) {
		BasicComponentsView componentsView = openComponentsView(browserType);

		Label hiddenDiv = componentsView.getLabel("#hiddenDiv");
		Assert.assertTrue(hiddenDiv.exists());
		Assert.assertFalse(hiddenDiv.isDisplayed());

		Label nothing = componentsView.getLabel("#idontexist");
		Assert.assertFalse(nothing.isDisplayed());
		Assert.assertFalse(nothing.exists());
	}

	@TestId(id = "CIP-3515-func-getAttribute", title = "Verify it's possible to get element attribute")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void getAttribute(BrowserType browserType) {
		BasicComponentsView componentsView = openComponentsView(browserType);
	
		UiComponent link = componentsView.getViewComponent("#linkId", UiComponent.class);
		Assert.assertEquals("linkId", link.getProperty("id"));
		Assert.assertEquals("commonClass", link.getProperty("class"));
		Assert.assertEquals("http://www.ericsson.se/", link.getProperty("href"));
		Assert.assertEquals("_blank", link.getProperty("target"));
	}
	
	@TestId(id = "CIP-3515-func-getText", title = "Verify it's possible to get element text")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void getText(BrowserType browserType) {
		BasicComponentsView componentsView = openComponentsView(browserType);
		
		// <a id="linkId" class="commonClass" href="http://www.ericsson.se/" target="_blank">Sample link</a>
		Assert.assertEquals("Sample link", componentsView.getLink().getText());
		// <div id="labelId" class="divClass commonClass">Div <b>bold</b> content</div>
		Assert.assertEquals("Div bold content", componentsView.getLabel("#labelId").getText());
		// <input type="text" id="textBoxId" class="commonClass" name="textBoxName" value="Sample text" />
		Assert.assertEquals("Sample text", componentsView.getTextBox("#textBoxId").getText());
		// <input type="button" id="buttonId" class="commonClass" name="buttonName" value="ButtonCaption" onclick="alert('Button clicked')"/>
		Assert.assertEquals("ButtonCaption", componentsView.getButton().getText());
		Assert.assertEquals("option3 title", componentsView.getSelect().getText());
	}
	
	@TestId(id = "CIP-3515-func-isVisible", title = "Verify it's possible to check if element is visible")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void isVisible(BrowserType browserType) {
		// Visibility and existence check is shown in the following test:
		isElementPresent(browserType);
	}

	@TestId(id = "CIP-3515-func-findElementByXpath", title = "Verify it's possible to find element by XPath")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void findElementByXpath(BrowserType browserType) {
		BrowserTab tab = openBrowserTab(browserType, "sso_applications.htm");
		Link link = tab.getGenericView().getLink(SelectorType.XPATH, "//a[@title='Launch OSS Common Explorer']");
		Assert.assertEquals(true, link.exists());
	}

	@TestId(id = "CIP-3515-func-findElementByXpath", title = "Verify it's possible to find few elements by XPath")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void findFewElementsByXpath(BrowserType browserType) {
		BrowserTab tab = openBrowserTab(browserType, "basic_components.htm");
		List<Link> allLinks = tab.getGenericView().getViewComponents(SelectorType.XPATH, "//a", Link.class);
		Assert.assertEquals(3, allLinks.size());
	}

	@TestId(id = "CIP-3515-func-addSelection", title = "Verify it's possible to work with multiple selects")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void addSelection(BrowserType browserType) {
		BrowserTab browserTab = openBrowserTab(browserType, "basic_components.htm");
		ViewModel view = browserTab.getGenericView();
		
		Select listbox = view.getSelect("#listboxId");
		// One option is selected by default 
		Assert.assertEquals(1,  listbox.getSelectedOptions().size());

		// Selecting another option 
		listbox.selectByValue("m_option2 value");
		Assert.assertEquals(2,  listbox.getSelectedOptions().size());

		// Trying to select a non-existing option
		try {
			listbox.selectByValue("no such value");
			Assert.fail("UiComponentNotFoundException expected");
		} catch (UiComponentNotFoundException e) {
		}
		Assert.assertEquals(2,  listbox.getSelectedOptions().size());
	}
	
	@TestId(id = "CIP-3515-func-getElementWidth", title = "Verify it's possible to get element width")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void getElementWidth(BrowserType browserType) {
		ViewModel view = openGenericView(browserType, "basic_components.htm");
		UiComponent component = view.getViewComponent("#textBoxId");
		UiComponentSize size = component.getSize();
		Assert.assertTrue(size.getWidth() > 0);
		Assert.assertTrue(size.getHeight() > 0);
	}
}
