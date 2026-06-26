package io.github.kushagrasinghga.toolkit.exception;

import io.github.kushagrasinghga.toolkit.dto.ErrorCode;

public class ValidationException extends CustomException {
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}
