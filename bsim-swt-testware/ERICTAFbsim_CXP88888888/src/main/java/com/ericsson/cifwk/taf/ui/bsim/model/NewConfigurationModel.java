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
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class NewConfigurationModel extends SwtViewModel {

    @UiComponentMapping("#0")
    private TextBox nameField;

    @UiComponentMapping("#1")
    private TextBox descriptionField;

    @UiComponentMapping("Open new Planned Configuration")
    private CheckBox openPlannedConfig;

    @UiComponentMapping("OK")
    private Button okButton;

    public void setName(String name) {
        nameField.setText(name);
    }

    public void setDescription(String description) {
        descriptionField.setText(description);
    }

    public void setOpenNewConfig(boolean open) {
        if (open) {
            openPlannedConfig.select();
        } else {
            openPlannedConfig.deselect();
        }
    }

    public void save() {
        okButton.click();
    }

}
