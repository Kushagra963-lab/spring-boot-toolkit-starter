package io.github.kushagrasinghga.toolkit.autoconfigure;

import io.github.kushagrasinghga.toolkit.advice.ApiResponseAdvice;
import io.github.kushagrasinghga.toolkit.audit.AuditAspect;
import io.github.kushagrasinghga.toolkit.exception.GlobalExceptionHandler;
import io.github.kushagrasinghga.toolkit.timer.ExecutionTimerAspect;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ToolkitAutoConfigurationTest {
    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ToolkitAutoConfiguration.class));

    @Test
    void registersDefaultToolkitBeans() {
        contextRunner.run(context -> assertThat(context)
                .hasSingleBean(ApiResponseAdvice.class)
                .hasSingleBean(GlobalExceptionHandler.class)
                .hasSingleBean(ExecutionTimerAspect.class)
                .hasSingleBean(AuditAspect.class)
                .hasBean("correlationIdFilterRegistration")
                .hasBean("requestLoggingFilterRegistration"));
    }

    @Test
    void disablesOptionalBeansWithProperties() {
        contextRunner
                .withPropertyValues(
                        "toolkit.response.enabled=false",
                        "toolkit.logging.enabled=false",
                        "toolkit.timer.enabled=false",
                        "toolkit.audit.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(ApiResponseAdvice.class);
                    assertThat(context).doesNotHaveBean(ExecutionTimerAspect.class);
                    assertThat(context).doesNotHaveBean(AuditAspect.class);
                    assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
                    assertThat(context).hasBean("correlationIdFilterRegistration");
                    assertThat(context).doesNotHaveBean("requestLoggingFilterRegistration");
                });
    }
}
