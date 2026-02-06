package com.houkunlin.common.time;

import tools.jackson.core.Version;
import tools.jackson.core.util.VersionUtil;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 配置 Jackson 对时间、日期的序列化、反序列化
 *
 * @author HouKunLin
 */
public class JavaTimeModule extends SimpleModule {
    private static final Version VERSION = VersionUtil.parseVersion("0.0.2", "com.houkunlin", "system-common-starter-java-time-module");

    public JavaTimeModule(
            DateTimeFormatter dateTimeDateTimeFormatter,
            DateTimeFormatter dateDateTimeFormatter,
            DateTimeFormatter timeDateTimeFormatter) {
        super(VERSION);

        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeDateTimeFormatter));
        addSerializer(LocalDate.class, new LocalDateSerializer(dateDateTimeFormatter));
        addSerializer(LocalTime.class, new LocalTimeSerializer(timeDateTimeFormatter));

        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeDateTimeFormatter));
        addDeserializer(LocalDate.class, new LocalDateDeserializer(dateDateTimeFormatter));
        addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeDateTimeFormatter));
    }
}
