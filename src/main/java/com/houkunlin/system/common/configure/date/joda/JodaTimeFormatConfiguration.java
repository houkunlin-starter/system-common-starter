package com.houkunlin.system.common.configure.date.joda;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * 配置joda时间、日期格式化对象
 *
 * @author HouKunLin
 */
@ConditionalOnProperty(prefix = "system.common.date", name = "joda.enable", matchIfMissing = true)
@Configuration
@ConditionalOnClass(JodaModule.class)
public class JodaTimeFormatConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(JodaTimeFormatConfiguration.class);
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
    public DateTimeFormatter dateTimeJodaDateTimeFormatter() {
        return new DateTimeFormatterBuilder().appendPattern(dateTimeFormat).toFormatter();
    }

    @Order(2)
    @Bean
    public DateTimeFormatter dateJodaDateTimeFormatter() {
        return new DateTimeFormatterBuilder().appendPattern(dateFormat).toFormatter();
    }

    @Order(3)
    @Bean
    public DateTimeFormatter timeJodaDateTimeFormatter() {
        return new DateTimeFormatterBuilder().appendPattern(timeFormat).toFormatter();
    }

    @ConditionalOnMissingBean
    @Bean
    public JodaDateTimeConverter jodaDateTimeConverter(List<DateTimeFormatter> formats) {
        return new JodaDateTimeConverter(formats);
    }

    @ConditionalOnMissingBean
    @Bean
    public JodaLocalDateConverter jodaLocalDateConverter(@Qualifier("dateJodaDateTimeFormatter") DateTimeFormatter formatter) {
        return new JodaLocalDateConverter(formatter);
    }

    @ConditionalOnMissingBean
    @Bean
    public JodaLocalDateTimeConverter jodaLocalDateTimeConverter(@Qualifier("dateTimeJodaDateTimeFormatter") DateTimeFormatter formatter) {
        return new JodaLocalDateTimeConverter(formatter);
    }

    @ConditionalOnMissingBean
    @Bean
    public JodaTimeModule jodaTimeModule(
            @Qualifier("dateTimeJodaDateTimeFormatter") DateTimeFormatter dateTimeJodaDateTimeFormatter,
            @Qualifier("dateJodaDateTimeFormatter") DateTimeFormatter dateJodaDateTimeFormatter,
            @Qualifier("timeJodaDateTimeFormatter") DateTimeFormatter timeJodaDateTimeFormatter) {
        return new JodaTimeModule(dateTimeJodaDateTimeFormatter, dateJodaDateTimeFormatter, timeJodaDateTimeFormatter);
    }

    @PostConstruct
    public void post() {
        logger.debug("自动配置 joda 日期对象转换配置");
    }
}
