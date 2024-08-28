package com.ericsson.cifwk.ve.plugins;

import com.ericsson.cifwk.ve.application.dto.EiffelMessage;

public interface DashboardResponseProcessor {
    String getMessageType();

    Object process(EiffelMessage message);
}
