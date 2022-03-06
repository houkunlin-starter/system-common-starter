package com.houkunlin.system.common.exception;

import com.houkunlin.system.common.IErrorModule;
import com.houkunlin.system.common.MessageWrapper;
import com.houkunlin.system.common.StackUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 业务异常对象。
 *
 * @author HouKunLin
 */
@Getter
public class BusinessException extends RuntimeException {
    public static final String DEFAULT_TITLE = "业务错误";
    private final String errorCode;
    private final String title;
    private final List<String> messages;

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.title = DEFAULT_TITLE;
        this.messages = Collections.emptyList();
    }

    public BusinessException(IErrorModule errorModule) {
        super(errorModule.getMessage());
        this.errorCode = errorModule.getErrorCode();
        this.title = DEFAULT_TITLE;
        this.messages = Collections.singletonList(errorModule.getMessage());
    }

    public BusinessException(String message) {
        super(message);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.title = DEFAULT_TITLE;
        this.messages = Collections.singletonList(message);
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.title = DEFAULT_TITLE;
        this.messages = Collections.singletonList(message);
    }

    public BusinessException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.title = DEFAULT_TITLE;
        this.messages = Collections.singletonList(message);
    }

    public BusinessException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
        this.title = DEFAULT_TITLE;
        this.messages = Collections.emptyList();
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.title = DEFAULT_TITLE;
        this.messages = Collections.singletonList(message);
    }

    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.title = DEFAULT_TITLE;
        this.messages = Collections.singletonList(message);
    }

    public BusinessException(final String title, final String... messages) {
        super(title);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.title = title;
        this.messages = Arrays.asList(messages);
    }

    public BusinessException(final String errorCode, final String title, final String... messages) {
        super(title);
        this.errorCode = errorCode;
        this.title = title;
        this.messages = Arrays.asList(messages);
    }

    public BusinessException(final String errorCode, final String title, final Throwable throwable, final String... messages) {
        super(title, throwable);
        this.errorCode = errorCode;
        this.title = title;
        this.messages = Arrays.asList(messages);
    }

    public BusinessException(final String title, final List<String> messages) {
        super(title);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.title = title;
        this.messages = messages;
    }

    public BusinessException(final String errorCode, final String title, final List<String> messages) {
        super(title);
        this.errorCode = errorCode;
        this.title = title;
        this.messages = messages;
    }

    public BusinessException(final String errorCode, final String title, final Throwable throwable, final List<String> messages) {
        super(title, throwable);
        this.errorCode = errorCode;
        this.title = title;
        this.messages = messages;
    }
}
