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
import com.ericsson.cifwk.taf.ui.sdk.CheckBox;
import com.ericsson.cifwk.taf.ui.sdk.Select;
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class GeneralTabModel extends SwtViewModel {

    @UiComponentMapping("Auto Integrate")
    private CheckBox autoIntegrate;

    @UiComponentMapping("{label = 'OSS Node Template:'}")
    private Select ossNodeTemplate;

    public void setAutoIntegrate(boolean autoIntegrate) {
        if (autoIntegrate) {
            this.autoIntegrate.select();
        } else {
            this.autoIntegrate.deselect();
        }
    }

    public void setNodeTemplate(String nodeTemplate) {
        ossNodeTemplate.selectByTitle(nodeTemplate);
    }

    public static class GeneralTabSubModel extends SwtViewModel {

        @UiComponentMapping("{label = 'Site:'}")
        private TextBox site;

        @UiComponentMapping("{label = 'Location:'}")
        private TextBox location;

        @UiComponentMapping("{label = 'O&M Host IP Address:'}")
        private TextBox ipAddress;

        public void setSite(String site) {
            this.site.setText(site);
        }

        public void setLocation(String location) {
            this.location.setText(location);
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress.setText(ipAddress);
        }

    }
}
