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

import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;

public class BsimTabModel extends SwtViewModel {

    private DesktopWindow window;

    @UiComponentMapping("{label = 'Planned Configuration Name:'}")
    private Select select;

    @UiComponentMapping("New Node:hover")
    private Button addButton;

    @UiComponentMapping("Import Nodes from file to list:hover")
    private Button importButton;

    @UiComponentMapping("Validate Nodes")
    private Button validateNodes;

    @UiComponentMapping("Add Nodes")
    private Button addNodesButton;

    public void setPlannedConfiguration(String configurationName) {
        select.selectByTitle(configurationName);
    }

    public void addNode() {
        addButton.click();
    }

    public void validate() {
        validateNodes.click();
    }

    public Button getAddNodesButton() {
        return addNodesButton;
    }

    public void importConfiguration(String configurationFileName) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                importButton.click();
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        String userInput = configurationFileName + "\r\n";
        window.sendKeys(userInput);
    }

    public BsimTabModel init(DesktopWindow window) {
        this.window = window;
        return this;
    }
}
