[![](https://jitpack.io/v/houkunlin/system-common-starter.svg)](https://jitpack.io/#houkunlin/system-common-starter)

# 系统公共配置 Starter

> v2.0.0 起基于 SpringBoot 4.0.2 编译发布，JDK 最低要求 JDK21

## 配置默认全局异常捕获

- `com.houkunlin.common.exception.BusinessException` 自定义业务异常对象

- `com.houkunlin.common.exception.GlobalRestControllerExceptionHandler`
- `com.houkunlin.common.exception.GlobalRestSecurityExceptionHandler`

## 配置默认日期解析转换

- `com.houkunlin.common.time.JavaTimeFormatConfiguration`

## 工具

- `com.houkunlin.common.RequestUtil`
  - `public static HttpServletRequest getRequest()`
  - `public static String getRequestIp()`
  - `public static String getRequestIp(HttpServletRequest request)`
- `com.houkunlin.common.ErrorMessage` 返回前端消息包裹器对象
