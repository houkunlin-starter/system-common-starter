package com.houkunlin.system.common;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 给前端返回的统一格式信息数据包装器。
 * 目前只用在返回错误消息，ok200的成功消息不适用该对象来再次封装
 *
 * @author HouKunLin
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class MessageWrapper {
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
    private final String msg;
    /**
     * 其他错误相关消息
     */
    private final Object data;

    public MessageWrapper(String code, String msg) {
        this(code, msg, null);
    }

    public MessageWrapper(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构建一个默认的错误消息
     *
     * @param msg 错误信息
     * @return 消息对象
     */
    public static MessageWrapper error(String msg) {
        return error(msg, null);
    }

    /**
     * 构建一个默认的错误消息
     *
     * @param msg  错误信息
     * @param data 错误其他相关内容
     * @return 消息对象
     */
    public static MessageWrapper error(String msg, Object data) {
        final StackTraceElement traceElement = StackUtil.getParentStackTraceElement(MessageWrapper.class);

        return new MessageWrapper(buildErrorCode(traceElement), msg, data);
    }

    /**
     * 构建一个默认的成功信息
     *
     * @param msg 成功信息
     * @return 消息对象
     */
    public static MessageWrapper ok(String msg) {
        return ok(msg, null);
    }

    /**
     * 构建一个默认的成功信息
     *
     * @param msg  成功信息
     * @param data 成功信息相关内容
     * @return 消息对象
     */
    public static MessageWrapper ok(String msg, Object data) {
        return new MessageWrapper("OK", msg, data);
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
