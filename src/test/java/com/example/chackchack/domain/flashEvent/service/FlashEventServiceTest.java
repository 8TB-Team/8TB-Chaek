package com.example.chackchack.domain.flashEvent.service;

import com.example.chackchack.domain.auth.dto.request.SignUpRequest;
import com.example.chackchack.domain.auth.service.AuthService;
import com.example.chackchack.domain.flashEvent.dto.request.EventCreateRequest;
import com.example.chackchack.domain.flashEvent.entity.EventMap;
import com.example.chackchack.domain.flashEvent.repository.EventMapRepository;
import com.example.chackchack.domain.flashEvent.repository.EventUserMapRepository;
import com.example.chackchack.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FlashEventServiceTest {

    @Autowired FlashEventService flashEventService;
    @Autowired EventMapRepository eventMapRepository;
    @Autowired EventUserMapRepository eventUserMapRepository;
    @Autowired AuthService authService;

    private final int testcase = 200;
    private final int maxParticipants = 50;

    private Long eventId;
    private EventMap eventMap;

    @BeforeEach
    void setUp() {
        EventCreateRequest request = new EventCreateRequest("title", "description", maxParticipants);
        eventMap = EventMap.from(request);
        eventId = eventMapRepository.save(eventMap).getId();

        for (int i = 0; i <= testcase; i++) {
            SignUpRequest signUpRequest = new SignUpRequest(
                    "user_"+i+"@test.com",
                    "testPW!@#"+i,
                    "nick_"+i,
                    UserRole.ROLE_USER);
            authService.signup(signUpRequest);
        }
    }

    @Test
    @DisplayName("비관적 락이 걸린 이벤트에 동시다발적으로 신청하는 경우")
    public void registerWithoutLock() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(testcase);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < testcase; i++) {
            final long userId = i+1;

            executorService.execute(() -> {
                try {
                    flashEventService.registerUserToEvent(eventId, userId);
                } catch (Exception e) {
                    System.out.println("예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double durationSeconds = duration / 1000.0;

        System.out.println("테스트 실행 시간: " + durationSeconds + "초");

        int registeredUserCount = eventUserMapRepository.countByEventMap(eventMap);
        assertEquals(registeredUserCount, maxParticipants);
    }
}
