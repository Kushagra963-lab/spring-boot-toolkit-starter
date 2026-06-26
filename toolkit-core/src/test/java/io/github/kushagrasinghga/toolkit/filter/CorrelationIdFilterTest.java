package io.github.kushagrasinghga.toolkit.filter;

import io.github.kushagrasinghga.toolkit.constants.ToolkitConstants;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class CorrelationIdFilterTest {
    @Test
    void createsCorrelationIdWhenMissing() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/health");
        MockHttpServletResponse response = new MockHttpServletResponse();

        new CorrelationIdFilter().doFilter(request, response, new MockFilterChain());

        assertThat(response.getHeader(ToolkitConstants.CORRELATION_ID_HEADER)).isNotBlank();
        assertThat(MDC.get(ToolkitConstants.CORRELATION_ID_MDC_KEY)).isNull();
    }

    @Test
    void reusesProvidedCorrelationId() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/health");
        request.addHeader(ToolkitConstants.CORRELATION_ID_HEADER, "known-id");
        MockHttpServletResponse response = new MockHttpServletResponse();

        new CorrelationIdFilter().doFilter(request, response, new MockFilterChain());

        assertThat(response.getHeader(ToolkitConstants.CORRELATION_ID_HEADER)).isEqualTo("known-id");
    }
}
