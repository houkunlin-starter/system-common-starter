package com.houkunlin.system.common.configure.date.joda;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.cfg.JacksonJodaDateFormat;
import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalTimeSerializer;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 配置 Jackson 对 joda 时间、日期的序列化、反序列化
 *
 * @author HouKunLin
 */
public class JodaTimeModule extends SimpleModule {
    private static final Logger logger = LoggerFactory.getLogger(JodaTimeModule.class);
    private static final Version VERSION = VersionUtil.parseVersion("0.0.2", "com.houkunlin", "system-common-starter-joda-time-module");

    public JodaTimeModule(
            DateTimeFormatter dateTimeJodaDateTimeFormatter,
            DateTimeFormatter dateJodaDateTimeFormatter,
            DateTimeFormatter timeJodaDateTimeFormatter) {
        super(VERSION);

        final JacksonJodaDateFormat jodaDateTimeFormat = new JacksonJodaDateFormat(dateTimeJodaDateTimeFormatter);
        final JacksonJodaDateFormat jodaDateFormat = new JacksonJodaDateFormat(dateJodaDateTimeFormatter);
        final JacksonJodaDateFormat jodaTimeFormat = new JacksonJodaDateFormat(timeJodaDateTimeFormatter);

        addSerializer(DateTime.class, new DateTimeSerializer(jodaDateTimeFormat));
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(jodaDateTimeFormat));
        addSerializer(LocalDate.class, new LocalDateSerializer(jodaDateFormat));
        addSerializer(LocalTime.class, new LocalTimeSerializer(jodaTimeFormat));

        final LocalTimeDeserializer localTimeDeserializer = new LocalTimeDeserializer(jodaTimeFormat);
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(jodaDateTimeFormat));
        addDeserializer(LocalDate.class, new LocalDateDeserializer(jodaDateFormat));
        addDeserializer(LocalTime.class, localTimeDeserializer);

        addDeserializer(DateTime.class, new JsonDeserializer<DateTime>() {
            final JsonDeserializer<DateTime> deserializer1 = DateTimeDeserializer.forType(DateTime.class);
            final DateTimeDeserializer deserializer2 = new DateTimeDeserializer(DateTime.class, jodaDateTimeFormat);

            final DateTime dateTime = new Instant(0L).toDateTime();

            @Override
            public DateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                try {
                    // 优先使用自定义的格式去解析
                    return (DateTime) deserializer2.deserialize(p, ctxt);
                } catch (Exception ignore) {
                }
                try {
                    // 使用默认的多种格式解析
                    return deserializer1.deserialize(p, ctxt);
                } catch (Exception ignore) {
                }
                if (logger.isWarnEnabled()) {
                    logger.warn("欲接收一个 org.joda.time.DateTime 对象， \"{}\" 可能是一个不支持的格式，请传输正确的数据格式", p.getValueAsString());
                }
                // 尝试解析时间格式
                return localTimeDeserializer.deserialize(p, ctxt).toDateTime(dateTime);
            }
        });
    }
}
