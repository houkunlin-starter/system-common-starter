package com.houkunlin.common;

import com.houkunlin.common.exception.BusinessException;

public interface IErrorMessageToException extends IErrorMessage {

    /**
     * 创建一个异常
     *
     * @return 异常
     */
    default BusinessException exception() {
        return new BusinessException(this);
    }

    /**
     * 创建一个异常
     *
     * @param messageArgs 格式化消息文本参数值
     * @return 异常
     */
    default BusinessException exception(Object... messageArgs) {
        return new BusinessException(this, messageArgs);
    }

    /**
     * 创建一个异常
     *
     * @param throwable 异常
     * @return 异常
     */
    default BusinessException exception(Throwable throwable) {
        return new BusinessException(this, throwable);
    }

    /**
     * 创建一个异常
     *
     * @param throwable   异常
     * @param messageArgs 格式化消息文本参数值
     * @return 异常
     */
    default BusinessException exception(Throwable throwable, Object... messageArgs) {
        return new BusinessException(this, throwable, messageArgs);
    }
}
