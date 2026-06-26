package io.github.kushagrasinghga.toolkit.autoconfigure;

import io.github.kushagrasinghga.toolkit.advice.ApiResponseAdvice;
import io.github.kushagrasinghga.toolkit.audit.AuditAspect;
import io.github.kushagrasinghga.toolkit.audit.AuditService;
import io.github.kushagrasinghga.toolkit.audit.LoggingAuditService;
import io.github.kushagrasinghga.toolkit.exception.GlobalExceptionHandler;
import io.github.kushagrasinghga.toolkit.filter.CorrelationIdFilter;
import io.github.kushagrasinghga.toolkit.filter.RequestLoggingFilter;
import io.github.kushagrasinghga.toolkit.properties.ToolkitProperties;
import io.github.kushagrasinghga.toolkit.timer.ExecutionTimerAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@AutoConfiguration
@ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
@EnableConfigurationProperties(ToolkitProperties.class)
public class ToolkitAutoConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "toolkit.response", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    ApiResponseAdvice apiResponseAdvice() {
        return new ApiResponseAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "correlationIdFilterRegistration")
    FilterRegistrationBean<CorrelationIdFilter> correlationIdFilterRegistration() {
        FilterRegistrationBean<CorrelationIdFilter> registration = new FilterRegistrationBean<>(new CorrelationIdFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    @ConditionalOnProperty(prefix = "toolkit.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = "requestLoggingFilterRegistration")
    FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterRegistration(ToolkitProperties properties) {
        FilterRegistrationBean<RequestLoggingFilter> registration = new FilterRegistrationBean<>(new RequestLoggingFilter(properties));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }

    @Bean
    @ConditionalOnProperty(prefix = "toolkit.timer", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    ExecutionTimerAspect executionTimerAspect() {
        return new ExecutionTimerAspect();
    }

    @Bean
    @ConditionalOnProperty(prefix = "toolkit.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    AuditService auditService() {
        return new LoggingAuditService();
    }

    @Bean
    @ConditionalOnProperty(prefix = "toolkit.audit", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }
}
