package com.houkunlin.system.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.houkunlin.system.common.MessageWrapper;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常捕获处理程序
 *
 * @author HouKunLin
 */
@RestControllerAdvice
public class GlobalRestControllerExceptionHandler {
    public static final String DEFAULT_ERROR_CODE_500 = "B" + HttpStatus.INTERNAL_SERVER_ERROR.value();
    public static final String DEFAULT_ERROR_MSG = "系统发生严重错误，请联系管理员";
    private static final Logger logger = LoggerFactory.getLogger(GlobalRestControllerExceptionHandler.class);
    private final HttpServletRequest request;

    public GlobalRestControllerExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 严重的错误，不在 @ExceptionHandler 捕获名单里面的错误
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public MessageWrapper exception(Throwable e) {
        logger.error("严重错误，从未考虑到的错误范围", e);
        return new MessageWrapper(DEFAULT_ERROR_CODE_500, DEFAULT_ERROR_MSG, Collections.singletonList(e.getMessage()));
    }

    /**
     * 其他未考虑到的所有错误
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Object exception(Exception e) {
        logger.error("严重错误，未捕获的其他异常", e);
        return new MessageWrapper(DEFAULT_ERROR_CODE_500, DEFAULT_ERROR_MSG, Collections.singletonList(e.getMessage()));
    }

    /**
     * 其他未考虑到的所有错误
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public Object businessException(BusinessException e) {
        logger.error("业务异常", e);
        return new MessageWrapper(e.getErrorCode(), e.getTitle(), e.getMessages());
    }

    /**
     * 客户端中止异常
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ClientAbortException.class)
    public Object clientAbortException(ClientAbortException e) {
        logger.error("{} {}?{} 客户端中止异常: {}", request.getMethod(), request.getRequestURI(), request.getQueryString(), e.getMessage());
        return new MessageWrapper(DEFAULT_ERROR_CODE_500, "客户端中止连接异常");
    }

    /**
     * 空指针错误
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public Object nullPointerException(NullPointerException e) {
        logger.error("空指针错误", e);
        return new MessageWrapper(DEFAULT_ERROR_CODE_500, DEFAULT_ERROR_MSG, Collections.singletonList("空指针错误"));
    }

    /**
     * Http请求方法不支持异常，请求一个未定义的 HttpMethod 方法。
     * 例如：
     * <p>定义了 @GetMapping("/user") ，但是使用了 POST、PUT、DELETE 请求了该 URI ，则抛出该异常</p>
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Object httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("Http请求方法不支持异常", e);
        List<String> message = new ArrayList<>();
        message.add(String.format("URI不支持 %s 请求", e.getMethod()));
        Set<HttpMethod> supportedHttpMethods = e.getSupportedHttpMethods();
        if (supportedHttpMethods != null) {
            String supportedMethods = supportedHttpMethods.stream().map(Enum::name).collect(Collectors.joining("/"));
            message.add(String.format("该URI可能支持 %s 请求", supportedMethods));
        }
        return new MessageWrapper("B" + HttpStatus.METHOD_NOT_ALLOWED.value(), "HTTP 请求方法不支持", message);
    }

    /**
     * WEB 404 错误，不启用 @EnableWebMvc 注解， spring.mvc.throw-exception-if-no-handler-found 配置失效，无法抛出404异常在这里捕获处理。
     * 如果要捕获404错误，请重新继承实现 BasicErrorController 功能
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class})
    public Object noHandlerFoundException(NoHandlerFoundException e) {
        logger.error("404错误", e);
        return new MessageWrapper("B" + HttpStatus.NOT_FOUND.value(), "找不到请求资源", Collections.singletonList(e.getMessage()));
    }

    /**
     * WEB 请求类型转换错误，请求的数据类型转换错误异常
     *
     * @param e 错误
     * @return json
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Object httpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("数据类型转换错误: {}", e.getLocalizedMessage());
        logger.error("数据类型转换错误", e);
        final Throwable cause = e.getCause();
        if (cause instanceof JsonMappingException) {
            // 自定义JSON反序列化器时，如果遇到解析异常则会执行到这里
            final HttpStatus status = HttpStatus.BAD_REQUEST;
            final Throwable throwable = cause.getCause();
            if (throwable instanceof BusinessException) {
                final BusinessException businessException = (BusinessException) throwable;
                final MessageWrapper wrapper = new MessageWrapper(businessException.getErrorCode(), businessException.getTitle(), businessException.getMessages());
                return new ResponseEntity<>(wrapper, status);
            }
            final MessageWrapper wrapper = new MessageWrapper("A" + status.value(), "数据类型转换错误", Collections.singleton(e.getMessage()));
            return new ResponseEntity<>(wrapper, status);
        }
        final MessageWrapper wrapper = new MessageWrapper(DEFAULT_ERROR_CODE_500, "数据类型转换错误", Collections.singleton(e.getMessage()));
        return new ResponseEntity<>(wrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * WEB 请求数据校验错误。使用 @Valid 和 @Validated 校验请求参数数据出现错误（检验不合格）时抛出这个异常（手动抛出）
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public Object bindException(BindException e) {
        logger.error("请求参数数据校验不通过", e);
        final List<String> messages = e.getAllErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return new MessageWrapper("A" + HttpStatus.BAD_REQUEST.value(), "请求参数数据校验不通过", messages);
    }
}
