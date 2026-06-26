package io.github.kushagrasinghga.toolkit.timer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ExecutionTimerAspect {
    private static final Logger log = LoggerFactory.getLogger(ExecutionTimerAspect.class);

    @Around("@annotation(io.github.kushagrasinghga.toolkit.annotation.TrackExecution) || @within(io.github.kushagrasinghga.toolkit.annotation.TrackExecution)")
    public Object trackExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            log.info("{} executed in {} ms", joinPoint.getSignature().toShortString(), elapsedMs);
        }
    }
}
