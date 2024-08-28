/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.taf.ui.bsim;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.ui.DesktopNavigator;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.bsim.model.BsimModel;
import com.ericsson.cifwk.taf.ui.bsim.model.BsimTabModel;
import org.junit.Test;

public class ImportConfiguration {

    @Test
    public void importConfiguration() {

//        SwtNavigator swtNavigator = new SwtNavigator(DataHandler.getHostByName("master"), "/opt/ericsson/nms_cex_client/bin/cex_client","/opt/ericsson/nms_cex_client/bin/cex_client_application.ini","192.168.0.86:0.0",300000L);

        // getting main view
        DesktopNavigator desktopNavigator = UI.newSwtNavigator(DataHandler.getHostByName("master"));
        DesktopWindow window = desktopNavigator.getWindowByTitle("OSS Common Explorer - valid configuration");
        BsimModel model = window.getView(BsimModel.class).init(window.getGenericView());

        // getting BSIM tab
        model.activateTab("BSIM");
        BsimTabModel bsimTab = window.getView(BsimTabModel.class).init(window);

        // import configuration scenario
        bsimTab.importConfiguration("/tmp/test.xml");
    }
}
