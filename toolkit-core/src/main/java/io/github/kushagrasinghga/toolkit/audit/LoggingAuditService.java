package io.github.kushagrasinghga.toolkit.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingAuditService implements AuditService {
    private static final Logger log = LoggerFactory.getLogger(LoggingAuditService.class);

    @Override
    public void record(AuditEvent event) {
        log.info("audit action={} user={} endpoint={} result={} durationMs={} correlationId={}",
                event.action(),
                event.username(),
                event.endpoint(),
                event.result(),
                event.executionTimeMs(),
                event.correlationId());
    }
}
