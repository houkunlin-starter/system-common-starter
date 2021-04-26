package com.houkunlin.system.common.exception;

import com.houkunlin.system.common.IErrorModule;
import com.houkunlin.system.common.MessageWrapper;
import com.houkunlin.system.common.StackUtil;
import lombok.Getter;

/**
 * 业务异常对象。
 *
 * @author HouKunLin
 */
@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
    }

    public BusinessException(IErrorModule errorModule) {
        super(errorModule.getMessage());
        this.errorCode = errorModule.getErrorCode();
    }

    public BusinessException(String message) {
        super(message);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BusinessException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = MessageWrapper.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
    }
}
