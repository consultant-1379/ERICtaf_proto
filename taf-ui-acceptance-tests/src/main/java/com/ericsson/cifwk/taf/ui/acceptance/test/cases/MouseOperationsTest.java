package com.ericsson.cifwk.taf.ui.acceptance.test.cases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.acceptance.test.data.MultiBrowserTestDataProvider;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

public class MouseOperationsTest extends AbstractTafUiAcceptanceTestCase {

	@BeforeClass
	public void setUp() {
		super.setUp();
	}
	
	@AfterMethod
	public void tearDown() {
		UI.closeAllWindows();
	}
	
	@TestId(id = "CIP-3515-func-mouseOver-and-mouseMoveAt", title = "Verify it's possible to simulate mouseover and mousemove events")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void mouseOverAndMove(BrowserType browserType) {
		ViewModel view = openGenericView(browserType, "basic_components.htm");
		UiComponent mouseButton = view.getViewComponent("#mouseButtonId");
		mouseButton.mouseOver();
		
		Assert.assertEquals("MOUSE_OVER", view.getViewComponent("#mouseButtonEventId").getText());
		Assert.assertEquals("MOUSE_MOVE", view.getViewComponent("#mouseMoveDetectorId").getText());
	}

	@TestId(id = "CIP-3515-func-mouseDown-and-mouseUp", title = "Verify it's possible to simulate mousedown and mouseup events")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void mouseDownAndUp(BrowserType browserType) {
		ViewModel view = openGenericView(browserType, "basic_components.htm");
		
		UiComponent mouseButton = view.getViewComponent("#mouseButtonId");
		mouseButton.mouseDown();
		Assert.assertEquals("MOUSE_DOWN", view.getViewComponent("#mouseButtonEventId").getText());

		mouseButton.mouseUp();
		Assert.assertEquals("MOUSE_UP", view.getViewComponent("#mouseButtonEventId").getText());
	}

	@TestId(id = "CIP-3515-func-mouseDown-and-mouseUp", title = "Verify it's possible to simulate mouseout event")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void mouseOut(BrowserType browserType) {
		ViewModel view = openGenericView(browserType, "basic_components.htm");
		
		UiComponent mouseButton = view.getViewComponent("#mouseButtonId");
		mouseButton.mouseOver();
		mouseButton.mouseOut();
		
		Assert.assertEquals("MOUSE_OUT", view.getViewComponent("#mouseButtonEventId").getText());
	}

	@TestId(id = "CIP-3515-func-dragAndDrop", title = "Verify it's possible to drag and drop element")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void dragAndDropToObject(BrowserType browserType) {	
		BrowserTab browserTab = openBrowserTab(browserType, "basic_components.htm");
		ViewModel view = browserTab.getGenericView();
		UiComponent dragAndDropState = view.getViewComponent("#dragAndDropStateId");
		Assert.assertEquals("", dragAndDropState.getText());

		UiComponent draggableItem = view.getViewComponent("#draggableOld");
		UiComponent dragTarget = view.getViewComponent("#dragTargetOld");
		
		browserTab.dragAndDropTo(draggableItem, dragTarget);
		
		Assert.assertEquals("DROPPED_TO_TARGET", dragAndDropState.getText());
	}
	
	@TestId(id = "CIP-3515-func-dragAndDropHtml5", title = "Verify it's possible to drag and drop element using HTML5 native events")
	@Test(dataProvider = "allBrowsers", dataProviderClass = MultiBrowserTestDataProvider.class)
	public void dragAndDrop_html5(BrowserType browserType) {
		BrowserTab browserTab = openBrowserTab(browserType, "basic_components.htm");
		ViewModel view = browserTab.getGenericView();

		UiComponent dragAndDropState = view.getViewComponent("#dragAndDropStateId");
		Assert.assertEquals("", dragAndDropState.getText());

		UiComponent draggableItem = view.getViewComponent("#draggable");
		UiComponent dragTarget = view.getViewComponent("#dragTarget");
		
		browserTab.dragAndDropTo(draggableItem, dragTarget);
		
		Assert.assertEquals("DROPPED_TO_TARGET", dragAndDropState.getText());
	}

}
