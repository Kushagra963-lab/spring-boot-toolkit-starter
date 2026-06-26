package io.github.kushagrasinghga.toolkit.dto;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp,
        String path,
        String correlationId
) {
    public static <T> ApiResponse<T> success(T data, String path, String correlationId) {
        return new ApiResponse<>(true, "Success", data, Instant.now(), path, correlationId);
    }
}
