package io.github.kushagrasinghga.toolkit.logging;

import io.github.kushagrasinghga.toolkit.constants.ToolkitConstants;
import org.slf4j.MDC;

public final class CorrelationId {
    private CorrelationId() {
    }

    public static String current() {
        return MDC.get(ToolkitConstants.CORRELATION_ID_MDC_KEY);
    }
}
