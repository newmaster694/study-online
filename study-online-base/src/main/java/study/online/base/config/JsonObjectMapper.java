package study.online.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JsonObjectMapper {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		log.info("JSON处理器启动...");

		return jacksonObjectMapperBuilder -> {
			// long -> string
			jacksonObjectMapperBuilder.serializerByType(
				Long.class,
				ToStringSerializer.instance);
			jacksonObjectMapperBuilder.serializerByType(
				BigInteger.class,
				ToStringSerializer.instance);

			//时间精度序列化
			jacksonObjectMapperBuilder.serializerByType(
				LocalDateTime.class,
				new LocalDateTimeSerializer(
					DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
			jacksonObjectMapperBuilder.serializerByType(
				LocalDate.class,
				new LocalDateSerializer(
					DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
			jacksonObjectMapperBuilder.serializerByType(
				LocalTime.class,
				new LocalTimeSerializer(
					DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

			//时间精度反序列化
			jacksonObjectMapperBuilder.deserializerByType(
				LocalDateTime.class,
				new LocalDateTimeDeserializer(
					DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
			jacksonObjectMapperBuilder.deserializerByType(
				LocalDate.class,
				new LocalDateDeserializer(
					DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
			jacksonObjectMapperBuilder.deserializerByType(
				LocalTime.class,
				new LocalTimeDeserializer(
					DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
		};
	}
}
