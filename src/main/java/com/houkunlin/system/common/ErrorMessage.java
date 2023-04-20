package com.houkunlin.system.common;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 给前端返回的统一格式信息数据包装器。
 * 目前只用在返回错误消息，ok200的成功消息不适用该对象来再次封装
 *
 * @author HouKunLin
 */
@Accessors(chain = true)
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class ErrorMessage implements IErrorMessage, Serializable {
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

    public ErrorMessage(IErrorMessage errorMessage) {
        this(errorMessage.getCode(), errorMessage.getMessage(), null);
    }

    public ErrorMessage(String code, String message) {
        this(code, message, null);
    }

    public ErrorMessage(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构建一个默认的错误消息
     *
     * @param message 错误信息
     * @return 消息对象
     */
    public static ErrorMessage error(String message) {
        return error(message, null);
    }

    /**
     * 构建一个默认的错误消息
     *
     * @param message 错误信息
     * @param data    错误其他相关内容
     * @return 消息对象
     */
    public static ErrorMessage error(String message, Object data) {
        final StackTraceElement traceElement = StackUtil.getParentStackTraceElement(ErrorMessage.class);

        return new ErrorMessage(buildErrorCode(traceElement), message, data);
    }

    /**
     * 构建一个默认的成功信息
     *
     * @return 消息对象
     */
    public static ErrorMessage ok() {
        return ok("OK", null);
    }

    /**
     * 构建一个默认的成功信息
     *
     * @param message 成功信息
     * @return 消息对象
     */
    public static ErrorMessage ok(String message) {
        return ok(message, null);
    }

    /**
     * 构建一个默认的成功信息
     *
     * @param message 成功信息
     * @param data    成功信息相关内容
     * @return 消息对象
     */
    public static ErrorMessage ok(String message, Object data) {
        return new ErrorMessage("OK", message, data);
    }

    /**
     * 构建错误代码
     *
     * @param traceElement 堆栈跟踪元素
     * @return 错误代码
     */
    public static String buildErrorCode(StackTraceElement traceElement) {
        if (traceElement != null) {
            // MN = Method Name， #L = Error Line Number
            return "MN:" + traceElement.getMethodName() + "#L" + traceElement.getLineNumber();
        }
        return "MN:[unknown]#L0";
    }
}
