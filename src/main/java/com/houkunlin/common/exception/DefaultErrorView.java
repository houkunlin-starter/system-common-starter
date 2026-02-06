package com.houkunlin.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * 自定义错误页面视图。
 *
 * @author HouKunLin
 * @see ErrorMvcAutoConfiguration.StaticView
 * @see ErrorMvcAutoConfiguration.WhitelabelErrorViewConfiguration#defaultErrorView
 * @since 1.0.2
 */
@RequiredArgsConstructor
public class DefaultErrorView implements View {
    private static final Logger logger = LoggerFactory.getLogger(DefaultErrorView.class);
    private static final MediaType TEXT_HTML_UTF8 = MediaType.APPLICATION_JSON;
    private final ObjectMapper objectMapper;

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    /**
     * 视图渲染
     *
     * @param model    页面变量信息
     * @param request  当前请求对象
     * @param response 当前响应对象
     * @throws Exception 处理异常
     * @see ErrorMvcAutoConfiguration.StaticView#render(java.util.Map, jakarta.servlet.http.HttpServletRequest, jakarta.servlet.http.HttpServletResponse)
     */
    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (response.isCommitted()) {
            logger.error("错误：{}", model);
            return;
        }
        response.setContentType(TEXT_HTML_UTF8.toString());
        objectMapper.writeValue(response.getOutputStream(), model);
    }
}
