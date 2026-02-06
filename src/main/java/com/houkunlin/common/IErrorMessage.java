package com.houkunlin.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 获取异常模块代码接口。
 * 每个应用模块都应该定义至少一个异常枚举，枚举中定义了当前应用模块所有的异常信息。
 *
 * @author HouKunLin
 */
public interface IErrorMessage {
    /**
     * 获取异常代码。
     *
     * @return 异常代码
     */
    String getCode();

    /**
     * 错误信息
     *
     * @return 错误信息
     */
    String getMessage();

    /**
     * 获取 HTTP 状态码
     *
     * @return 状态码
     */
    @JsonIgnore
    default int getHttpStatusCode() {
        return -1;
    }

    /**
     * 错误信息
     *
     * @param args 格式化消息文本参数值
     * @return 错误信息
     */
    default String getMessage(Object... args) {
        return getMessage().formatted(args);
    }

    /**
     * 附带对象内容
     *
     * @return 对象内容
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    default Object getData() {
        return null;
    }

    /**
     * 转换成消息对象
     *
     * @return 消息对象
     */
    default ErrorMessage toErrorMessage() {
        return new ErrorMessage(getCode(), getMessage(), getData());
    }

    /**
     * 转换成消息对象
     *
     * @param args 格式化消息文本参数值
     * @return 消息对象
     */
    default ErrorMessage toErrorMessage(Object... args) {
        return new ErrorMessage(getCode(), getMessage(args), getData());
    }
}
