package com.houkunlin.system.common.configure.date.joda;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * {@link DateTime org.joda.time.DateTime} 转换
 *
 * @author HouKunLin
 */
public class JodaDateTimeConverter implements Converter<String, DateTime> {
    private final List<DateTimeFormatter> formats;

    public JodaDateTimeConverter(List<DateTimeFormatter> formats) {
        this.formats = formats;
    }

    @Override
    public DateTime convert(@NonNull String source) {
        for (DateTimeFormatter format : formats) {
            try {
                return format.parseDateTime(source);
            } catch (Exception ignored) {
            }
        }
        try {
            return DateTime.parse(source);
        } catch (Exception ignore) {
        }
        throw new IllegalArgumentException("\"" + source + "\" 无法找到合适的格式转换成 org.joda.time.DateTime 对象");
    }
}
