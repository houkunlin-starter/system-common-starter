package com.houkunlin.common;

import lombok.Getter;

/**
 * 全局错误信息
 *
 * @author HouKunLin
 */
@Getter
public enum GlobalErrorMessage implements IErrorMessage, IErrorMessageToException {
    /**
     * 默认错误
     */
    DEFAULT_ERROR("B500", "系统发生严重错误，请联系管理员"),
    /**
     * 客户端中止连接
     */
    CLIENT_ABORT("B500", "客户端中止连接"),
    /**
     * 系统发生了空指针调用错误
     */
    NULL_POINTER("B500", "系统发生了空指针调用错误"),
    /**
     * HTTP 请求方法不支持
     */
    METHOD_NOT_ALLOWED("B405", "HTTP 请求方法不支持"),
    /**
     * 找不到请求资源
     */
    NOT_FOUND("B404", "找不到请求资源"),
    /**
     * 数据类型转换错误
     */
    HTTP_MESSAGE_CONVERT_JSON_MAPPING("A:CONVERT:JSON:400", "数据类型转换错误"),
    /**
     * 数据类型转换错误
     */
    HTTP_MESSAGE_CONVERT("A:CONVERT:500", "数据类型转换错误"),
    /**
     * 请求参数数据校验不通过
     */
    METHOD_BIND("A400", "请求参数数据校验不通过"),
    /**
     * 权限认证错误
     */
    UNAUTHORIZED("B401", "权限认证错误"),
    /**
     * 拒绝访问异常
     */
    FORBIDDEN("B403", "权限认证错误，拒绝访问资源"),
    /**
     * 请求参数数据校验不通过
     */
    PATH_VARIABLE("A500", "路径参数解析错误，缺少路径变量"),
    /**
     * 请求不满足的参数条件
     */
    UNSATISFIED_REQUEST_PARAMETER("A500", "请求缺少参数，或不满足参数条件"),
    ;

    private final String code;
    private final String message;

    GlobalErrorMessage(String message) {
        this.code = name();
        this.message = message;
    }

    GlobalErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
