package com.houkunlin.common.aop.ip;

import com.houkunlin.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 允许指定IP访问接口
 *
 * @author HouKunLin
 */
@Accessors(chain = true)
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class AllowIPException extends BusinessException {
    /**
     * 当前请求IP。当前用来匹配的IP。
     */
    private String currentIp;
    /**
     * <p>是否设置了白名单IP。</p>
     * <p>true：有白名单IP，但是当前请求IP未在白名单IP内</p>
     * <p>false：没有白名单IP，直接失败</p>
     */
    private boolean hasAllowIp = false;

    public AllowIPException(String message) {
        super(message);
    }

    public AllowIPException(String message, Throwable cause) {
        super(message, cause);
    }

    public AllowIPException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getCode() {
        return "B403";
    }

    @Override
    public int getHttpStatusCode() {
        return 403;
    }

    @Override
    public Object getData() {
        return Map.of("currentIp", currentIp, "whitelist", hasAllowIp);
    }
}
