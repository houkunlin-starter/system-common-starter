package com.houkunlin.system.common.configure.date.local;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Qualifier;

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
            @Qualifier("dateTimeDateTimeFormatter") DateTimeFormatter dateTimeDateTimeFormatter,
            @Qualifier("dateDateTimeFormatter") DateTimeFormatter dateDateTimeFormatter,
            @Qualifier("timeDateTimeFormatter") DateTimeFormatter timeDateTimeFormatter) {
        super(VERSION);

        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeDateTimeFormatter));
        addSerializer(LocalDate.class, new LocalDateSerializer(dateDateTimeFormatter));
        addSerializer(LocalTime.class, new LocalTimeSerializer(timeDateTimeFormatter));

        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeDateTimeFormatter));
        addDeserializer(LocalDate.class, new LocalDateDeserializer(dateDateTimeFormatter));
        addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeDateTimeFormatter));
    }
}
