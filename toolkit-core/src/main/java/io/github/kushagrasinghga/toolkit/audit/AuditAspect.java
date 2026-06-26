package io.github.kushagrasinghga.toolkit.audit;

import io.github.kushagrasinghga.toolkit.annotation.Audit;
import io.github.kushagrasinghga.toolkit.logging.CorrelationId;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
public class AuditAspect {
    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Around("@annotation(io.github.kushagrasinghga.toolkit.annotation.Audit) || @within(io.github.kushagrasinghga.toolkit.annotation.Audit)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        Audit annotation = resolveAuditAnnotation(joinPoint);
        long start = System.nanoTime();
        String result = "SUCCESS";
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            result = "FAILED";
            throw throwable;
        } finally {
            auditService.record(new AuditEvent(
                    currentUser(),
                    Instant.now(),
                    currentEndpoint(),
                    annotation.action(),
                    (System.nanoTime() - start) / 1_000_000,
                    result,
                    CorrelationId.current()));
        }
    }

    private Audit resolveAuditAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Audit methodAudit = AnnotationUtils.findAnnotation(signature.getMethod(), Audit.class);
        if (methodAudit != null) {
            return methodAudit;
        }
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), Audit.class);
    }

    private String currentEndpoint() {
        HttpServletRequest request = currentRequest();
        return request == null ? null : request.getMethod() + " " + request.getRequestURI();
    }

    private String currentUser() {
        HttpServletRequest request = currentRequest();
        if (request == null || request.getUserPrincipal() == null) {
            return "anonymous";
        }
        return request.getUserPrincipal().getName();
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }
}
