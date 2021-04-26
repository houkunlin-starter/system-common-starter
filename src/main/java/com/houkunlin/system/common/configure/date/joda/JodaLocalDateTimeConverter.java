package com.houkunlin.system.common.configure.date.joda;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * {@link LocalDateTime org.joda.time.LocalDateTime} 转换
 *
 * @author HouKunLin
 */
public class JodaLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    private final DateTimeFormatter formatter;

    public JodaLocalDateTimeConverter(@Qualifier("dateTimeJodaDateTimeFormatter") DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalDateTime convert(@NonNull String source) {
        try {
            return formatter.parseLocalDateTime(source);
        } catch (Exception ignored) {
        }
        try {
            return LocalDateTime.parse(source);
        } catch (Exception ignore) {
        }
        throw new IllegalArgumentException("\"" + source + "\" 无法找到合适的格式转换成 org.joda.time.LocalDateTime 对象");
    }
}
