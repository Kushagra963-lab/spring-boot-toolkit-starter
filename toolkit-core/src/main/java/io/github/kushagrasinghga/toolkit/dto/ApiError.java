package io.github.kushagrasinghga.toolkit.dto;

import java.time.Instant;
import java.util.List;

public record ApiError(
        boolean success,
        String error,
        String message,
        List<String> details,
        Instant timestamp,
        String path,
        String correlationId
) {
    public static ApiError of(ErrorCode error, String message, List<String> details, String path, String correlationId) {
        return new ApiError(false, error.name(), message, details, Instant.now(), path, correlationId);
    }
}
