package io.github.kushagrasinghga.toolkit.advice;

import io.github.kushagrasinghga.toolkit.annotation.IgnoreResponseWrapper;
import io.github.kushagrasinghga.toolkit.dto.ApiError;
import io.github.kushagrasinghga.toolkit.dto.ApiResponse;
import io.github.kushagrasinghga.toolkit.logging.CorrelationId;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.hasMethodAnnotation(IgnoreResponseWrapper.class)
                && !returnType.getContainingClass().isAnnotationPresent(IgnoreResponseWrapper.class);
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (!MediaType.APPLICATION_JSON.includes(selectedContentType)
                || body instanceof ApiResponse<?>
                || body instanceof ApiError
                || body instanceof Resource
                || body instanceof byte[]
                || body instanceof String) {
            return body;
        }

        return ApiResponse.success(body, currentPath(), CorrelationId.current());
    }

    private String currentPath() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        }
        return null;
    }
}
