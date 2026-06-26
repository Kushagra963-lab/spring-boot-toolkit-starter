package io.github.kushagrasinghga.toolkit.audit;

import java.time.Instant;

public record AuditEvent(
        String username,
        Instant timestamp,
        String endpoint,
        String action,
        long executionTimeMs,
        String result,
        String correlationId
) {
}
