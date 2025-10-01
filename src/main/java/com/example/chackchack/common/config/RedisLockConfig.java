package com.example.chackchack.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 설정
 * Lettuce를 이용한 Redis 설정
 */
@Configuration
public class RedisLockConfig {


    /**
     * ReidsTemplate 설정
     * Lock 구현을 위한 String 타입 직렬화
     */

    @Bean
    public RedisTemplate<String, String> lockRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);

        return redisTemplate;
    }

}
