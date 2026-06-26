package io.github.kushagrasinghga.toolkit.advice;

import io.github.kushagrasinghga.toolkit.dto.ApiResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseAdviceTest {
    @Test
    void wrapsJsonBody() {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("GET", "/api/products");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        Object response = new ApiResponseAdvice().beforeBodyWrite(
                Map.of("id", 1),
                null,
                MediaType.APPLICATION_JSON,
                null,
                null,
                null);

        assertThat(response).isInstanceOf(ApiResponse.class);
        assertThat(((ApiResponse<?>) response).path()).isEqualTo("/api/products");

        RequestContextHolder.resetRequestAttributes();
    }
}
