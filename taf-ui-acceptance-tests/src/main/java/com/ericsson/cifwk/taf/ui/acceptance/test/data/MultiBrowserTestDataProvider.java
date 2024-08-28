package com.ericsson.cifwk.taf.ui.acceptance.test.data;

import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.TestData;
import com.ericsson.cifwk.taf.ui.BrowserType;

public class MultiBrowserTestDataProvider implements TestData {
	@DataProvider(name = "allBrowsers")
	public static Object[][] listOfBrowsersObjects() {
		return new Object[][] {
			{ BrowserType.HEADLESS },
			{ BrowserType.FIREFOX },
			{ BrowserType.CHROME },
			{ BrowserType.IEXPLORER }
		};
	}

	@DataProvider(name = "headless")
	public static Object[][] headlessOnly() {
		return new Object[][] {
			{ BrowserType.HEADLESS }
		};
	}
}
