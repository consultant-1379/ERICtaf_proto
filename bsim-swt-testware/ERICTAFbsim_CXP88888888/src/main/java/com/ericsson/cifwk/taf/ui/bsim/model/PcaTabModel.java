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

import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Table;

public class PcaTabModel extends SwtViewModel {

    @UiComponentMapping("{type = 'org.eclipse.swt.widgets.Table', index = 0}")
    private Table configurationTable;

    @UiComponentMapping("New Planned Configuration:hover")
    private Button newConfigurationButton;

    public boolean configurationExists(String configurationName) {
        int hpc1RowIndex = configurationTable.getRowIndex(configurationName, "Name");
        return hpc1RowIndex >= 0;
    }

    public void selectConfiguration(String configurationName) {
        configurationTable.select(configurationTable.getRowIndex(configurationName, "Name"));
    }

    public void addConfiguration() {
        newConfigurationButton.click();
    }


}
