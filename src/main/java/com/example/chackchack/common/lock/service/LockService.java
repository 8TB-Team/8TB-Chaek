package com.example.chackchack.common.lock.service;

import com.example.chackchack.common.lock.exception.InvalidLockException;
import com.example.chackchack.common.lock.exception.LockErrorCode;
import com.example.chackchack.common.lock.repository.LockRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;


@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {

    private final LockRedisRepository lockRedisRepository;

    // Lock 설정
    private static final Duration LOCK_DURATION = Duration.ofSeconds(5); //Lock 유지 시간
    private static final long RETRY_DELAY_MS = 50; // 재시도 간격
    private static final long MAX_RETRY_COUNT = 100; // 최대 재시도 횟수


    /**
     * Lock 획득 (Spin Lock 방식)
     *
     * Lock 획득에 실패하면 일정 시간 대기 후 재시도
     * 최대 재시도 횟수를 초과하면 예외 발생
     */
    private void acquireLock(String lockKey, String lockValue) {
        int retryCount = 0;

        while (retryCount < MAX_RETRY_COUNT) {
            boolean acquired = lockRedisRepository.tryLock(lockKey, lockValue, LOCK_DURATION);

            if (acquired) {
                log.debug("Lock 획득 성공 - lockKey: {}, retryCount: {}", lockKey, retryCount);
                return;
            }

            // Lock 획득 실패 시 대기
            retryCount++;
            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new InvalidLockException(LockErrorCode.LOCK_INTERRUPTED);
            }
        }

        // 최대 재시도 횟수 초과
        log.error("Lock 획득 실패 - lockKey: {}, maxRetryCount: {}", lockKey, MAX_RETRY_COUNT);
        throw new InvalidLockException(LockErrorCode.LOCK_ACQUISITION_FAILED);
    }

    /**
     * Lock 해제
     *
     * Repository의 Lua 스크립트 기반 unlock을 사용하여
     * 안전하게 본인이 획득한 Lock만 해제
     */
    private void releaseLock(String lockKey, String lockValue) {
        try {
            boolean released = lockRedisRepository.unlock(lockKey, lockValue);

            if (released) {
                log.debug("Lock 해제 성공 - lockKey: {}", lockKey);
            } else {
                log.warn("Lock 해제 실패 - lockKey: {} (이미 만료되었거나 다른 스레드가 소유)", lockKey);
            }
        } catch (Exception e) {
            log.error("Lock 해제 중 예외 발생 - lockKey: {}", lockKey, e);
        }
    }

    /**
     * Lock을 획득하고 비즈니스 로직 실행 후 Lock 해제
     *
     * @param lockKey Lock의 식별자
     * @param <T> 실행할 비즈니스 로직
     */
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        String lockValue = UUID.randomUUID().toString();

        try {
            // Lock 획득 시도
            acquireLock(lockKey, lockValue);

            // 비즈니스 로직 실행
            log.debug("비즈니스 로직 실행 시작 - lockKey: {}", lockKey);
            return supplier.get();

        } finally {
            // Lock 해제
            releaseLock(lockKey, lockValue);
        }
    }
}
