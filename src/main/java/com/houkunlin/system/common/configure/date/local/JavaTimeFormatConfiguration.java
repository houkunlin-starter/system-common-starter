package com.houkunlin.system.common.configure.date.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 配置时间、日期格式化对象
 *
 * @author HouKunLin
 */
@ConditionalOnProperty(prefix = "system.common.date", name = "local.enable", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class JavaTimeFormatConfiguration {
    /**
     * Date格式化字符串
     */
    @Value("${spring.mvc.format.date:'yyyy-MM-dd'}")
    private String dateFormat;
    /**
     * Time格式化字符串
     */
    @Value("${spring.mvc.format.time:'HH:mm:ss'}")
    private String timeFormat;
    /**
     * DateTime格式化字符串
     */
    @Value("${spring.jackson.date-format:${spring.mvc.format.date-time:'yyyy-MM-dd HH:mm:ss'}}")
    private String dateTimeFormat;

    @ConditionalOnMissingBean
    @Bean
    public DateConverter dateConverter() {
        return new DateConverter(List.of(
                new SimpleDateFormat(dateTimeFormat),
                new SimpleDateFormat(dateFormat),
                new SimpleDateFormat(timeFormat)
        ));
    }

    @ConditionalOnMissingBean
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule(DateTimeFormatter.ofPattern(dateTimeFormat),
                DateTimeFormatter.ofPattern(dateFormat),
                DateTimeFormatter.ofPattern(timeFormat));
    }
}
