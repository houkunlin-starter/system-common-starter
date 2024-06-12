package com.houkunlin.system.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.houkunlin.system.common.BindFieldErrorMessage;
import com.houkunlin.system.common.ErrorMessage;
import com.houkunlin.system.common.GlobalErrorMessage;
import com.houkunlin.system.common.IErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * 全局异常捕获处理程序
 *
 * @author HouKunLin
 */
@RestControllerAdvice
public class GlobalRestControllerExceptionHandler {
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
    public ErrorMessage exception(Throwable e) {
        logger.error("严重错误，从未考虑到的错误范围", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        return GlobalErrorMessage.DEFAULT_ERROR.toErrorMessage();
    }

    /**
     * 其他未考虑到的所有错误
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorMessage exception(Exception e) {
        logger.error("严重错误，未捕获的其他异常", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        return GlobalErrorMessage.DEFAULT_ERROR.toErrorMessage();
    }

    /**
     * 其他未考虑到的所有错误
     *
     * @param e 错误
     * @return json
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorMessage> businessException(BusinessException e) {
        logger.error("业务异常", e);
        HttpStatus httpStatus;
        int statusCode = e.httpStatusCode();
        if (statusCode <= 0) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else {
            httpStatus = HttpStatus.valueOf(statusCode);
        }
        return new ResponseEntity<>(e.toErrorMessage(), httpStatus);
    }

    /**
     * 客户端中止异常
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ClientAbortException.class)
    public ErrorMessage clientAbortException(ClientAbortException e) {
        logger.error("{} {}?{} 客户端中止异常: {}", request.getMethod(), request.getRequestURI(), request.getQueryString(), e.getMessage());
        return GlobalErrorMessage.CLIENT_ABORT.toErrorMessage();
    }

    /**
     * 空指针错误
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public ErrorMessage nullPointerException(NullPointerException e) {
        logger.error("空指针错误", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        return GlobalErrorMessage.NULL_POINTER.toErrorMessage();
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
    public ErrorMessage httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("Http请求方法不支持异常", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        // List<String> message = new ArrayList<>();
        // message.add(String.format("URI不支持 %s 请求", e.getMethod()));
        // Set<HttpMethod> supportedHttpMethods = e.getSupportedHttpMethods();
        // if (supportedHttpMethods != null) {
        //     String supportedMethods = supportedHttpMethods.stream().map(HttpMethod::name).collect(Collectors.joining("/"));
        //     message.add(String.format("该URI可能支持 %s 请求", supportedMethods));
        // }
        return GlobalErrorMessage.METHOD_NOT_ALLOWED.toErrorMessage();
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
    public ErrorMessage noHandlerFoundException(NoHandlerFoundException e) {
        logger.error("404错误", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        return GlobalErrorMessage.NOT_FOUND.toErrorMessage();
    }

    /**
     * 找不到静态资源
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class})
    public ErrorMessage noResourceFoundException(NoResourceFoundException e) {
        logger.error("404错误", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        return GlobalErrorMessage.NOT_FOUND.toErrorMessage();
    }

    /**
     * WEB 请求类型转换错误，请求的数据类型转换错误异常
     *
     * @param e 错误
     * @return json
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorMessage> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("数据类型转换错误: {}", e.getLocalizedMessage());
        logger.error("数据类型转换错误", e);
        if (e instanceof IErrorMessage errorMessage) {
            return new ResponseEntity<>(errorMessage.toErrorMessage(), HttpStatus.BAD_REQUEST);
        }
        final Throwable cause = e.getCause();
        if (cause instanceof JsonMappingException jsonMappingException) {
            // 自定义JSON反序列化器时，如果遇到解析异常则会执行到这里
            final Throwable throwable = jsonMappingException.getCause();
            if (throwable instanceof IErrorMessage errorMessage) {
                return new ResponseEntity<>(errorMessage.toErrorMessage(), HttpStatus.BAD_REQUEST);
            }

            final ErrorMessage errorMessage = GlobalErrorMessage.HTTP_MESSAGE_CONVERT_JSON_MAPPING.toErrorMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        final ErrorMessage errorMessage = GlobalErrorMessage.HTTP_MESSAGE_CONVERT.toErrorMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * WEB 请求数据校验错误。使用 @Valid 和 @Validated 校验请求参数数据出现错误（检验不合格）时抛出这个异常（手动抛出）
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})
    public ErrorMessage bindException(BindException e) {
        logger.error("请求参数数据校验不通过", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        final List<BindFieldErrorMessage> messages = e.getAllErrors()
                .stream()
                .map(objectError -> {
                    BindFieldErrorMessage.BindFieldErrorMessageBuilder builder = BindFieldErrorMessage.builder();
                    builder.objectName(objectError.getObjectName());
                    builder.message(objectError.getDefaultMessage());
                    if (objectError instanceof FieldError fieldError) {
                        builder.fieldName(fieldError.getField());
                        builder.fieldValue(fieldError.getRejectedValue());
                    }
                    return builder.build();
                })
                .toList();
        return GlobalErrorMessage.METHOD_BIND.toErrorMessage().setData(messages);
    }

    /**
     * 路径参数绑定错误，缺少路径变量
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({MissingPathVariableException.class})
    public ErrorMessage missingPathVariableException(MissingPathVariableException e) {
        logger.error("路径参数解析错误，缺少路径变量", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        return GlobalErrorMessage.PATH_VARIABLE.toErrorMessage().setData(List.of(e.getVariableName()));
    }

    /**
     * 请求缺少参数，或不满足参数条件
     *
     * @param e 错误
     * @return json
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
    public ErrorMessage unsatisfiedServletRequestParameterException(UnsatisfiedServletRequestParameterException e) {
        logger.error("请求缺少参数，或不满足参数条件", e);
        if (e instanceof IErrorMessage errorMessage) {
            return errorMessage.toErrorMessage();
        }
        String[] paramConditions = e.getParamConditions();
        return GlobalErrorMessage.UNSATISFIED_REQUEST_PARAMETER.toErrorMessage().setData(paramConditions);
    }
}
