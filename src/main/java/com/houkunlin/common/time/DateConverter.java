package com.houkunlin.common.time;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 普通 {@link Date java.util.Date} 转换
 *
 * @author HouKunLin
 */
public class DateConverter implements Converter<String, Date> {
    private final Map<Integer, Set<SimpleDateFormat>> caches = new HashMap<>();

    public DateConverter(List<SimpleDateFormat> formats) {
        for (SimpleDateFormat format : formats) {
            final int key = format.toPattern().length();
            final Set<SimpleDateFormat> set = caches.getOrDefault(key, new HashSet<>());
            set.add(format);
            caches.put(key, set);
        }
    }

    @Override
    public Date convert(String source) {
        final Set<SimpleDateFormat> formatSet = caches.get(source.length());
        if (formatSet == null || formatSet.isEmpty()) {
            throw new IllegalArgumentException("\"" + source + "\" 无法找到合适的格式转换成 java.util.Date 对象");
        }

        for (SimpleDateFormat format : formatSet) {
            try {
                return format.parse(source);
            } catch (ParseException ignored) {
            }
        }
        throw new IllegalArgumentException("\"" + source + "\" 无法找到合适的格式转换成 java.util.Date 对象");
    }
}
