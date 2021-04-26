package com.houkunlin.system.common.configure.date.local;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 配置时间、日期格式化对象
 *
 * @author HouKunLin
 */
@Configuration
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

    @Order(1)
    @Bean
    public SimpleDateFormat dateTimeSimpleDateFormat() {
        return new SimpleDateFormat(dateTimeFormat);
    }

    @Order(2)
    @Bean
    public SimpleDateFormat dateSimpleDateFormat() {
        return new SimpleDateFormat(dateFormat);
    }

    @Order(3)
    @Bean
    public SimpleDateFormat timeSimpleDateFormat() {
        return new SimpleDateFormat(timeFormat);
    }

    @Order(1)
    @Bean
    public DateTimeFormatter dateTimeDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(dateTimeFormat);
    }

    @Order(2)
    @Bean
    public DateTimeFormatter dateDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(dateFormat);
    }

    @Order(3)
    @Bean
    public DateTimeFormatter timeDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(timeFormat);
    }

    @ConditionalOnMissingBean
    @Bean
    public DateConverter dateConverter(List<SimpleDateFormat> formats) {
        return new DateConverter(formats);
    }

    @ConditionalOnMissingBean
    @Bean
    public JavaTimeModule javaTimeModule(@Qualifier("dateTimeDateTimeFormatter") DateTimeFormatter dateTimeDateTimeFormatter,
                                         @Qualifier("dateDateTimeFormatter") DateTimeFormatter dateDateTimeFormatter,
                                         @Qualifier("timeDateTimeFormatter") DateTimeFormatter timeDateTimeFormatter) {
        return new JavaTimeModule(dateTimeDateTimeFormatter, dateDateTimeFormatter, timeDateTimeFormatter);
    }
}
