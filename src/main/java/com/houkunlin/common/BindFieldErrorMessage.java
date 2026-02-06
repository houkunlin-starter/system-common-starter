package com.houkunlin.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 参数校验失败的字段错误信息
 *
 * @author HouKunLin
 */
@Data
@Builder
@AllArgsConstructor
public class BindFieldErrorMessage implements Serializable {
    /**
     * 字段值
     */
    private final Object fieldValue;
    /**
     * 对象名称
     */
    private String objectName;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 错误信息
     */
    private String message;
}
