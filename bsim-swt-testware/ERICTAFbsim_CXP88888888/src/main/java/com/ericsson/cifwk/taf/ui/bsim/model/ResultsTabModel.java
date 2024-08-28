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
import com.ericsson.cifwk.taf.ui.sdk.SwtViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Table;

public class ResultsTabModel extends SwtViewModel {

    @UiComponentMapping("{type = 'org.eclipse.swt.widgets.Table', index = 1}")
    private Table resultTable;

    public boolean nodeExists(String nodeName) {
        int hpc1RowIndex = resultTable.getRowIndex(nodeName, "Node Name");
        return hpc1RowIndex >= 0;
    }

}
