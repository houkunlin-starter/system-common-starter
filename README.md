[![](https://jitpack.io/v/houkunlin/system-common-starter.svg)](https://jitpack.io/#houkunlin/system-common-starter)

# 系统公共配置 Starter

> v1.1.0 起基于 SpringBoot 3.0.0 编译发布，JDK 最低要求 JDK17
>
> v1.2.0 重构了
> [BusinessException.java](src%2Fmain%2Fjava%2Fcom%2Fhoukunlin%2Fsystem%2Fcommon%2Fexception%2FBusinessException.java) /
> [ErrorMessage.java](src%2Fmain%2Fjava%2Fcom%2Fhoukunlin%2Fsystem%2Fcommon%2FErrorMessage.java) /
> [IErrorMessage.java](src%2Fmain%2Fjava%2Fcom%2Fhoukunlin%2Fsystem%2Fcommon%2FIErrorMessage.java) /
> [RestfulErrorAttributes.java](src%2Fmain%2Fjava%2Fcom%2Fhoukunlin%2Fsystem%2Fcommon%2Fexception%2FRestfulErrorAttributes.java)
> 结构发生了变化，因此返回的错误提示格式也发生了变化

## 配置默认全局异常捕获

- `com.houkunlin.system.common.exception.BusinessException` 自定义业务异常对象

- `com.houkunlin.system.common.exception.GlobalRestControllerExceptionHandler`
- `com.houkunlin.system.common.exception.GlobalRestSecurityExceptionHandler`

## 配置默认日期解析转换

如需配置 joda 的日期对象转换，请直接引入 `com.fasterxml.jackson.datatype:jackson-datatype-joda` 依赖，而不是单独引入 joda
依赖

- `com.houkunlin.system.common.configure.date.local.JavaTimeFormatConfiguration`
- `com.houkunlin.system.common.configure.date.joda.JodaTimeFormatConfiguration`

## 工具

- `com.houkunlin.system.common.RequestUtil`
  - `public static HttpServletRequest getRequest()`
  - `public static String getRequestIp()`
  - `public static String getRequestIp(HttpServletRequest request)`
- `com.houkunlin.system.common.ErrorMessage` 返回前端消息包裹器对象
