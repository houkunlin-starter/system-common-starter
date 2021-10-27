package com.houkunlin.system.common.exception;

import com.houkunlin.system.common.MessageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

/**
 * 捕获 Spring Security 的异常
 *
 * @author HouKunLin
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ConditionalOnClass(value = {AccessDeniedException.class, AuthenticationException.class})
public class GlobalRestSecurityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalRestSecurityExceptionHandler.class);

    /**
     * 权限认证错误
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public Object authenticationException(AuthenticationException e) {
        logger.error("权限相关错误", e);
        return new MessageWrapper("B" + HttpStatus.UNAUTHORIZED.value(), "权限认证错误，未授权", Collections.singletonList(e.getMessage()));
    }

    /**
     * 拒绝访问异常，在访问有权限限制的方法时（@PreAuthorize），如果校验失败则在这里处理异常
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Object authenticationException(AccessDeniedException e) {
        logger.error("拒绝访问异常", e);
        return new MessageWrapper("B" + HttpStatus.FORBIDDEN.value(), "权限认证错误，拒绝访问资源", Collections.singletonList(e.getMessage()));
    }
}
