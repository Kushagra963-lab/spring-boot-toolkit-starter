package io.github.kushagrasinghga.toolkit.audit;

public interface AuditService {
    void record(AuditEvent event);
}
