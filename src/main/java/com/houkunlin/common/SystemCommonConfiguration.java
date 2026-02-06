package com.houkunlin.common;

import com.houkunlin.common.exception.DefaultErrorView;
import com.houkunlin.common.exception.GlobalRestControllerExceptionHandler;
import com.houkunlin.common.exception.GlobalRestSecurityExceptionHandler;
import com.houkunlin.common.exception.RestfulErrorAttributes;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import tools.jackson.databind.ObjectMapper;

/**
 * @author HouKunLin
 */
@AutoConfiguration(before = ErrorMvcAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class SystemCommonConfiguration {

    @ConditionalOnProperty(prefix = "system.common.error", name = "to-json", matchIfMissing = true)
    @Bean("error")
    public DefaultErrorView defaultErrorView(ObjectMapper objectMapper) {
        return new DefaultErrorView(objectMapper);
    }

    @ConditionalOnProperty(prefix = "system.common.error", name = "to-json", matchIfMissing = true)
    @Bean
    public RestfulErrorAttributes restfulErrorAttributes() {
        return new RestfulErrorAttributes();
    }

    @ConditionalOnMissingBean
    @Bean
    public GlobalRestControllerExceptionHandler globalRestControllerExceptionHandler(final HttpServletRequest request) {
        return new GlobalRestControllerExceptionHandler(request);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(value = {AccessDeniedException.class, AuthenticationException.class})
    public static class GlobalRestSecurityExceptionHandlerConfiguration {

        @ConditionalOnMissingBean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        @Bean
        public GlobalRestSecurityExceptionHandler globalRestSecurityExceptionHandler() {
            return new GlobalRestSecurityExceptionHandler();
        }
    }
}
