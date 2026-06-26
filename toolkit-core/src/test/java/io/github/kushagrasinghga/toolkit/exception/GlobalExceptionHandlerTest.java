package io.github.kushagrasinghga.toolkit.exception;

import io.github.kushagrasinghga.toolkit.dto.ApiError;
import io.github.kushagrasinghga.toolkit.dto.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void mapsResourceNotFoundTo404() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/products/99");

        ResponseEntity<ApiError> response = handler.handleCustomException(
                new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "missing"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(response.getBody().success()).isFalse();
    }
}
