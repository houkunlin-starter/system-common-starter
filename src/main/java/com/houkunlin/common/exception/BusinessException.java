package com.houkunlin.common.exception;

import com.houkunlin.common.ErrorMessage;
import com.houkunlin.common.IErrorMessage;
import com.houkunlin.common.StackUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;

/**
 * 业务异常对象。
 *
 * @author HouKunLin
 */
@Accessors(chain = true)
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException implements IErrorMessage {
    public static final String DEFAULT_TITLE = "Oops! 遇到了一个错误！";
    /**
     * 业务错误代码。参考《阿里巴巴Java开发手册》，每个错误对应一个错误代码
     * 规则：一位错误生产来源+四位数字编号。
     * 错误生产来源：
     * A：错误来源于用户
     * B：错误来源于当前系统
     * C：错误来源于第三方服务
     */
    private final String code;
    /**
     * 业务错误提示，详细的错误提示内容。
     */
    private final String message;
    /**
     * 其他错误相关消息
     */
    private Object data;
    /**
     * 设置返回的 HTTP 状态码，未设置此值时默认为 400 HttpStatus.BAD_REQUEST
     */
    private int httpStatusCode = -1;

    public BusinessException(IErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.code = errorMessage.getCode();
        this.message = errorMessage.getMessage();
        this.data = errorMessage.getData();
        this.httpStatusCode = errorMessage.getHttpStatusCode();
    }

    public BusinessException(IErrorMessage errorMessage, Object... messageArgs) {
        super(errorMessage.getMessage());
        this.code = errorMessage.getCode();
        if (messageArgs == null || messageArgs.length == 0) {
            this.message = errorMessage.getMessage();
        } else {
            this.message = errorMessage.getMessage(messageArgs);
        }
        this.data = errorMessage.getData();
        this.httpStatusCode = errorMessage.getHttpStatusCode();
    }

    public BusinessException(Throwable throwable) {
        super(throwable);
        this.code = ErrorMessage.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.message = DEFAULT_TITLE;
        this.data = Collections.singletonList(throwable.getMessage());
    }

    public BusinessException(IErrorMessage errorMessage, Throwable throwable) {
        super(errorMessage.getMessage(), throwable);
        this.code = errorMessage.getCode();
        this.message = errorMessage.getMessage();
        this.data = errorMessage.getData();
        this.httpStatusCode = errorMessage.getHttpStatusCode();
    }

    public BusinessException(IErrorMessage errorMessage, Throwable throwable, Object... messageArgs) {
        super(errorMessage.getMessage(), throwable);
        this.code = errorMessage.getCode();
        if (messageArgs == null || messageArgs.length == 0) {
            this.message = errorMessage.getMessage();
        } else {
            this.message = errorMessage.getMessage(messageArgs);
        }
        this.data = errorMessage.getData();
        this.httpStatusCode = errorMessage.getHttpStatusCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ErrorMessage.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.message = message;
        this.data = null;
    }

    public BusinessException(String message, Throwable throwable) {
        super(message, throwable);
        this.code = ErrorMessage.buildErrorCode(StackUtil.getParentStackTraceElement(BusinessException.class));
        this.message = message;
        this.data = null;
    }
}
