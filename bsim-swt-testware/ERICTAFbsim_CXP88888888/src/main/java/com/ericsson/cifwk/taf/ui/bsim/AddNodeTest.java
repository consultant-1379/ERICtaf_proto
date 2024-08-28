package com.ericsson.cifwk.taf.ui.bsim;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.ui.DesktopNavigator;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.bsim.model.BsimModel;
import com.ericsson.cifwk.taf.ui.bsim.model.BsimTabModel;
import com.ericsson.cifwk.taf.ui.bsim.model.GeneralTabModel;
import com.ericsson.cifwk.taf.ui.bsim.model.NewConfigurationModel;
import com.ericsson.cifwk.taf.ui.bsim.model.PcaTabModel;
import com.ericsson.cifwk.taf.ui.bsim.model.ResultsTabModel;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AddNodeTest {

    private final static String CONFIG_TO_USE = "HPC1";

    private BsimModel model;
    private ViewModel view;
    private DesktopWindow window;
    private DesktopNavigator desktopNavigator;

    @Before
    public void setUp() {
        desktopNavigator = UI.newSwtNavigator(DataHandler.getHostByName("master"));
        window = desktopNavigator.getWindowByTitle("OSS Common Explorer - valid configuration");
        view = window.getGenericView();
        model = window.getView(BsimModel.class).init(view);
    }

    @Test
    public void modelView() {

        // activating PCA tab
        activateTab("PCA");

        PcaTabModel pcaTabModel = getView(PcaTabModel.class);

        // creating Planned Configuration on demand
        if (!pcaTabModel.configurationExists(CONFIG_TO_USE)) {
            pcaTabModel.addConfiguration();

            NewConfigurationModel newConfigurationModel = desktopNavigator.getWindowByTitle("New Planned Configuration").getView(NewConfigurationModel.class);
            newConfigurationModel.setName(CONFIG_TO_USE);
            newConfigurationModel.setDescription("Configuration for UI test purposes");
            newConfigurationModel.setOpenNewConfig(false);
            newConfigurationModel.save();
        }

        // selecting Planned Configuration
        pcaTabModel.selectConfiguration(CONFIG_TO_USE);
        model.sleep(3000);

        /*
         * Add Node
         */
        activateTab("BSIM");
        BsimTabModel bsimTabModel = window.getView(BsimTabModel.class);
        bsimTabModel.setPlannedConfiguration(CONFIG_TO_USE);
        bsimTabModel.addNode();

        activateTab("General");
        model.sleep(3000);
        GeneralTabModel generalTabModel = window.getView(GeneralTabModel.class);
        generalTabModel.setAutoIntegrate(false);
        generalTabModel.setNodeTemplate("AddNodeandSiteExample");
        model.sleep(1000);
        GeneralTabModel.GeneralTabSubModel generalTabSubModel = window.getView(GeneralTabModel.GeneralTabSubModel.class);
        generalTabSubModel.setSite("Test Site");
        generalTabSubModel.setLocation("Test Location");
        generalTabSubModel.setIpAddress("10.10.10.10");


        bsimTabModel.validate();
        model.clickAsync(bsimTabModel.getAddNodesButton());

        ViewModel confirmAddNodes = desktopNavigator.getWindowByTitle("Confirm Add Nodes").getGenericView();
        Button confirmAddNodesButton = confirmAddNodes.getButton("OK");
        confirmAddNodesButton.click();

        activateTab("Results");
        ResultsTabModel resultsTabModel = getView(ResultsTabModel.class);
        assertTrue(resultsTabModel.nodeExists("New_eRBS1"));
    }

    private <T extends ViewModel> T getView(Class<T> viewModelClass) {
        return window.getView(viewModelClass);
    }

    private void activateTab(String tabName) {
        model.activateTab(tabName);
    }
}
