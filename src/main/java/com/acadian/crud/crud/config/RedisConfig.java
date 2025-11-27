package com.acadian.crud.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String,Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {

        RedisSerializationContext<String,Object> serializationContext = RedisSerializationContext
                .<String,Object>newSerializationContext( new StringRedisSerializer())
                .value(new GenericJackson2JsonRedisSerializer())
                .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }

    @Bean
    public Duration employeeCacheTTL() {
        return Duration.ofMinutes(10);
    }
}
