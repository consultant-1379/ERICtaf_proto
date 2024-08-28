package com.ericsson.cifwk.taf.ui.acceptance.test.operators;

import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.acceptance.test.viewmodels.BasicComponentsView;

public interface TafUiOperator {
	
	BasicComponentsView openComponentsView();
	
	BrowserTab openBrowserTab(String htmlPage);
	
	Browser getBrowser();
	
	int getOpenedWindowsAmount();
	
	BrowserTab openBrowserTabInNewBrowser(String htmlPage);
}
