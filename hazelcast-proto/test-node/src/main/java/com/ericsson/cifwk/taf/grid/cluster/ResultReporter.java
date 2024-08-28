package com.ericsson.cifwk.taf.grid.cluster;

import com.ericsson.cifwk.taf.grid.shared.NodeInstruction;
import com.ericsson.cifwk.taf.grid.shared.TestUpdate;

/**
 *
 */
public interface ResultReporter {

    void publish(NodeInstruction instruction, TestUpdate update);

}
