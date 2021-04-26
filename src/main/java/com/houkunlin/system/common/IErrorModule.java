package com.houkunlin.system.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 获取异常模块代码接口。
 * 每个应用模块都应该定义至少一个异常枚举，枚举中定义了当前应用模块所有的异常信息。
 *
 * @author HouKunLin
 */
public interface IErrorModule {
    /**
     * 获取异常模块代码。
     *
     * @return 异常模块代码
     */
    String getModuleCode();

    /**
     * 获取错误代码（错误编号、业务编号）
     *
     * @return 错误代码
     */
    int getNumber();

    /**
     * 错误信息
     *
     * @return 错误信息
     */
    String getMessage();

    /**
     * 结合异常模块获取错误代码
     *
     * @return 业务错误代码
     */
    default String getErrorCode() {
        return getModuleCode() + StringUtils.leftPad(String.valueOf(getNumber()), 4, "0");
    }
}
