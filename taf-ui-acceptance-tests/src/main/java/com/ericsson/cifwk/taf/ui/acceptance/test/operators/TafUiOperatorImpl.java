package com.ericsson.cifwk.taf.ui.acceptance.test.operators;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;

import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.BrowserType;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.acceptance.test.viewmodels.BasicComponentsView;
import com.ericsson.cifwk.taf.utils.FileFinder;

public class TafUiOperatorImpl implements TafUiOperator, UiOperator {
	private final BrowserType browserType;
	private Browser browser;

	public TafUiOperatorImpl(BrowserType browserType) {
		this.browserType = browserType;
	}

	public BasicComponentsView openComponentsView() {
		BrowserTab browserTab = openBrowserTab("basic_components.htm");
		return browserTab.getView(BasicComponentsView.class);
	}
	
	private String getFullUrl(String htmlPage) {
		return String.format("http://localhost:%s/%s", TestHelper.getJettyPort(), htmlPage);
	}

	@Override
	public Browser getBrowser() {
		return browser;
	}

	@Override
	public int getOpenedWindowsAmount() {
		return (browser != null && !browser.isClosed()) ? browser.getOpenedWindowsAmount() : 0;
	}

	public BrowserTab openBrowserTab(String htmlPage) {
		if (browser == null || browser.isClosed()) {
			this.browser = UI.newBrowser(getBrowserType());
		}
		return browser.open(getFullUrl(htmlPage));
	}

	public BrowserTab openBrowserTabInNewBrowser(String htmlPage) {
		Browser browser = UI.newBrowser(getBrowserType());
		return browser.open(getFullUrl(htmlPage));
	}

	protected BrowserType getBrowserType() {
		return browserType;
	}

	protected String findHtmlPage(String fileName) {
		List<String> findFile = FileFinder.findFile(fileName);
		if (CollectionUtils.isEmpty(findFile)) {
			Assert.fail("'" + fileName + "' not found!");
		}
		return "file:///" + findFile.get(0);
	}
}
