/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.taf.ui.bsim.model;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class BsimModel extends SwtViewModel {

    private ViewModel genericView;

    public BsimModel init(ViewModel genericView) {
        this.genericView = genericView;
        return this;
    }

    public void activateTab(String tabName) {
        String selector = String.format("{wrapperType = 'org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem', type = 'org.eclipse.swt.custom.CTabItem', mnemonicText='%s', initActions = ['activate']}", tabName);
        genericView.getViewComponent(selector);
    }

    public void clickAsync(final UiComponent clickable) {
        Executors.newSingleThreadExecutor().submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                clickable.click();
                return null;
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.interrupted();
        }
    }

}
