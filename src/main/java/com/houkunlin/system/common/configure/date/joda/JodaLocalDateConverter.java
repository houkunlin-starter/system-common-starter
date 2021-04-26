package com.houkunlin.system.common.configure.date.joda;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

/**
 * {@link LocalDate org.joda.time.LocalDate} 转换
 *
 * @author HouKunLin
 */
public class JodaLocalDateConverter implements Converter<String, LocalDate> {
    private final DateTimeFormatter formatter;

    public JodaLocalDateConverter(@Qualifier("dateJodaDateTimeFormatter") DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalDate convert(@NonNull String source) {
        try {
            return formatter.parseLocalDate(source);
        } catch (Exception ignored) {
        }
        try {
            return LocalDate.parse(source);
        } catch (Exception ignore) {
        }
        throw new IllegalArgumentException("\"" + source + "\" 无法找到合适的格式转换成 org.joda.time.LocalDate 对象");
    }
}
