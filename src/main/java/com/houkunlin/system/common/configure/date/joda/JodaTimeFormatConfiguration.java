package com.houkunlin.system.common.configure.date.joda;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * 配置joda时间、日期格式化对象
 *
 * @author HouKunLin
 */
@ConditionalOnProperty(prefix = "system.common.date", name = "joda.enable", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(JodaModule.class)
public class JodaTimeFormatConfiguration {
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
    public JodaDateTimeConverter jodaDateTimeConverter() {
        return new JodaDateTimeConverter(List.of(
                new DateTimeFormatterBuilder().appendPattern(dateTimeFormat).toFormatter(),
                new DateTimeFormatterBuilder().appendPattern(dateFormat).toFormatter(),
                new DateTimeFormatterBuilder().appendPattern(timeFormat).toFormatter()
        ));
    }

    @ConditionalOnMissingBean
    @Bean
    public JodaLocalDateConverter jodaLocalDateConverter() {
        return new JodaLocalDateConverter(new DateTimeFormatterBuilder().appendPattern(dateFormat).toFormatter());
    }

    @ConditionalOnMissingBean
    @Bean
    public JodaLocalDateTimeConverter jodaLocalDateTimeConverter() {
        return new JodaLocalDateTimeConverter(new DateTimeFormatterBuilder().appendPattern(dateTimeFormat).toFormatter());
    }

    @ConditionalOnMissingBean
    @Bean
    public JodaTimeModule jodaTimeModule() {
        return new JodaTimeModule(new DateTimeFormatterBuilder().appendPattern(dateTimeFormat).toFormatter(),
                new DateTimeFormatterBuilder().appendPattern(dateFormat).toFormatter(),
                new DateTimeFormatterBuilder().appendPattern(timeFormat).toFormatter());
    }
}
