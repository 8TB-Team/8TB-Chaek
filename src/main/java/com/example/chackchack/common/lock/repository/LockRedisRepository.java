package com.example.chackchack.common.lock.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LockRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // Lua 스크립트 (Lock 해제용)
    private static final String UNLOCK_LUA_SCRIPT =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "   return redis.call('del', KEYS[1]) " +
                    "else " +
                    "   return 0 " +
                    "end";


    /**
     * Lock 획득 시도
     *
     * @param key Lock의 식별자
     * @param value Lock을 요청한 주체 식별자
     * @param duration Lock 만료 시간
     * @return Lock 획득 성공 여부
     */
    public Boolean tryLock(String key, String value, Duration duration) {
        try {
            Boolean result = redisTemplate.opsForValue()
                    .setIfAbsent(key, value, duration);

            log.debug("Lock 획득 시도 - key: {}, result: {}", key, result);
            return result != null && result;
        } catch (Exception e) {
            log.error("Lock 획득 중 오류 발생 - key: {}", key, e);
            return false;
        }
    }

    /**
     * Lock 해제 (Lua 스크립트 적용)
     */
    public Boolean unlock(String key, String value) {
        try {
            Long result = redisTemplate.execute(
                    new DefaultRedisScript<>(UNLOCK_LUA_SCRIPT, Long.class),
                    Collections.singletonList(key),
                    value
            );

            boolean unlocked = result != null && result == 1;

            if (unlocked) {
                log.debug("Lock 해제 성공 - key: {}", key);
            } else {
                log.warn("Lock 해제 실패 - key: {} (이미 만료되었거나 다른 스레드가 소유)", key);
            }

            return unlocked;

        } catch (Exception e) {
            log.error("Lock 해제 중 오류 발생 - key: {}", key, e);
            return false;
        }
    }

    /**
     * Lock 존재 여부 확인
     */
    public Boolean hasLock(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
