package com.houkunlin.system.common.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * 把 {@link BasicErrorController} 的异常信息抛出使用统一异常处理器进行处理
 *
 * @author HouKunLin
 * @see BasicErrorController
 * @see BasicErrorController#errorHtml(jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
 * @see BasicErrorController#error(jakarta.servlet.http.HttpServletRequest)
 * @see AbstractErrorController#getErrorAttributes(jakarta.servlet.http.HttpServletRequest, org.springframework.boot.web.error.ErrorAttributeOptions)
 * @see DefaultErrorAttributes
 * @since 1.0.2
 */
@Component
@ConditionalOnProperty(prefix = "system.common.error", name = "to-json", matchIfMissing = true)
public class RestfulErrorAttributes implements ErrorAttributes {
    private static final String ERROR_INTERNAL_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    /**
     * 获取错误属性信息
     *
     * @param webRequest 当前的web请求
     * @param options
     * @return
     * @see AbstractErrorController#getErrorAttributes(jakarta.servlet.http.HttpServletRequest, org.springframework.boot.web.error.ErrorAttributeOptions)
     */
    @Override
    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error = getError(webRequest);
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = error.getCause();
            }
            errorAttributes.put("code", "A500");
            errorAttributes.put("msg", error.getMessage());
        } else {
            final HttpStatus status = getStatus(webRequest);
            errorAttributes.put("code", "A" + status.value());
            errorAttributes.put("msg", "请求资源错误，请检查是否存在资源");
        }

        return errorAttributes;
    }

    /**
     * 获取异常信息
     *
     * @param webRequest 当前请求
     * @return 异常信息
     * @see DefaultErrorAttributes#getError(org.springframework.web.context.request.WebRequest)
     */
    @Override
    public Throwable getError(@NonNull final WebRequest webRequest) {
        Throwable exception = getAttribute(webRequest, ERROR_INTERNAL_ATTRIBUTE);
        if (exception == null) {
            exception = getAttribute(webRequest, RequestDispatcher.ERROR_EXCEPTION);
        }
        // store the exception in a well-known attribute to make it available to metrics
        // instrumentation.
        webRequest.setAttribute(ErrorAttributes.ERROR_ATTRIBUTE, exception, SCOPE_REQUEST);
        return exception;
    }

    /**
     * 获取属性信息
     *
     * @param requestAttributes 当前请求属性信息
     * @param name              属性名称
     * @param <T>               属性类型
     * @return 属性对象
     * @see DefaultErrorAttributes#getAttribute(org.springframework.web.context.request.RequestAttributes, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, SCOPE_REQUEST);
    }

    /**
     * 获取状态码
     *
     * @param requestAttributes 请求属性
     * @return HTTP状态码
     * @see AbstractErrorController#getStatus(jakarta.servlet.http.HttpServletRequest)
     */
    protected HttpStatus getStatus(RequestAttributes requestAttributes) {
        Integer statusCode = getAttribute(requestAttributes, RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
