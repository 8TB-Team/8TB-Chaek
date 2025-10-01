package com.example.chackchack.domain.reservation.service;

import com.example.chackchack.domain.book.entity.Book;
import com.example.chackchack.domain.book.repository.BookRepository;
import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.bookItem.repository.BookItemRepository;
import com.example.chackchack.domain.reservation.dto.request.ReservationCreateRequest;
import com.example.chackchack.domain.reservation.dto.response.ReservationResponse;
import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.reservation.entity.ReservationStatus;
import com.example.chackchack.domain.reservation.repository.ReservationRepository;
import com.example.chackchack.domain.user.entity.User;
import com.example.chackchack.domain.user.enums.UserRole;
import com.example.chackchack.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class ReservationConcurrencyTest {

    @Autowired
    private ReservationExternalService reservationExternalService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookItemRepository bookItemRepository;

    @Autowired
    private UserRepository userRepository;

    private BookItem testBookItem;
    private List<User> testUsers = new ArrayList<>();
    private static final int THREAD_COUNT = 10;

    @BeforeEach
    void setup() {
        // 테스트 데이터 초기화
        reservationRepository.deleteAll();
        bookRepository.deleteAll();
        bookItemRepository.deleteAll();
        userRepository.deleteAll();

        // 테스트용 Book 생성
        Book testBook = new Book("테스트 저자", "테스트 카테고리", "테스트 도서");
        bookRepository.save(testBook);

        // 테스트용 BookItem 생성
        testBookItem = BookItem.builder()
                .book(testBook)
                .build();
        bookItemRepository.save(testBookItem);

        // 테스트용 User 생성
        //testUsers = createTestUsers(THREAD_COUNT);
    }

    @Test
    @DisplayName("동시에 여러 사용자가 같은 도서를 예약할 때 우선순위 중복 발생")
    void ReservationWithOutLock() throws InterruptedException {

        // given
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

        //여러 스레드가 동사에 add해도 안전
        Set<Integer> priorities = ConcurrentHashMap.newKeySet();

        //동시성 환경에서 안전하게 카운트 증가
        AtomicInteger successCount = new AtomicInteger(0);

        testUsers = createTestUsers(THREAD_COUNT);

        //when
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                            testBookItem.getSerialNumber());

                    ReservationResponse reservationResponse = reservationExternalService.createReservation(
                            testUsers.get(index).getId(),
                            reservationCreateRequest);

                    priorities.add(reservationResponse.getPriority());
                    successCount.incrementAndGet();

                } catch (Exception e) {
                    System.out.println("예약 실패" + e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        //then
        System.out.println("성공한 예약 수: " + successCount.get());
        System.out.println("고유한 우선순위 수: " + priorities.size());
        System.out.println("우선순위 목록: " + priorities);

        List<Reservation> savedReservations = reservationRepository
                .findByBookItemIdAndReservationStatusOrderByPriorityAsc(
                        testBookItem.getId(),
                        ReservationStatus.WAITING
                );
        List<Integer> savedPriorities = savedReservations.stream()
                .map(Reservation::getPriority)
                .collect(Collectors.toList());

        // 같은 우선순위가 중복으로 발생했는지 사이즈 비교
        assertThat(savedPriorities).hasSize(successCount.get());

        // 증복된 우선순위가 있는지 논리적 검증
        for (int i = 0; i < savedPriorities.size(); i++) {
            assertThat(savedPriorities.get(i)).isEqualTo(i + 1);
        }

    }

    @Test
    @DisplayName("Lock 적용 후 : 고부하 상황에서도 우선순위 정확성 유지")
    void ReservationWithLock() throws InterruptedException {

        //given
        int highLoadThreadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(highLoadThreadCount);
        CountDownLatch countDownLatch = new CountDownLatch(highLoadThreadCount);

        List<User> manyUsers = createTestUsers(highLoadThreadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        //when
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < highLoadThreadCount; i++) {
            final int index = i;
            executorService.execute(() -> {

                try {
                    ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                            testBookItem.getSerialNumber()
                    );

                    ReservationResponse reservationResponse = reservationExternalService.createReservation(
                            manyUsers.get(index).getId(),
                            reservationCreateRequest
                    );
                    successCount.incrementAndGet();
                }catch (Exception e) {
                    // 예외 무시
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();

        //then
        List<Reservation> savedReservations = reservationRepository
                .findByBookItemIdAndReservationStatusOrderByPriorityAsc(
                        testBookItem.getId(),
                        ReservationStatus.WAITING
                );

        List<Integer> savedPriorities = savedReservations.stream()
                .map(Reservation::getPriority)
                .collect(Collectors.toList());

        System.out.println("=== 고부하 테스트 결과 ===");
        System.out.println("총 요청 수: " + highLoadThreadCount);
        System.out.println("성공 수: " + successCount.get());
        System.out.println("소요 시간: " + (endTime - startTime) + "ms");
        System.out.println("초당 처리량: " + (successCount.get() * 1000.0 / (endTime - startTime)) + " TPS");

        // 검증
        // 성공횟수와 성공횟수 비교
        assertThat(savedReservations).hasSize(successCount.get());

        //우선 순위가 올바르게 정렬되었는지 체크
        for (int i = 0; i < savedPriorities.size(); i++) {
            assertThat(savedPriorities.get(i)).isEqualTo(i + 1);
        }

        assertThat(new HashSet<>(savedPriorities)).hasSize(successCount.get());

        System.out.println("고부하 동시성 테스트 통과");


    }

    // 유저 생성 메서드
    private List<User> createTestUsers(int count) {
        return java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> {
                    User user = User.builder()
                            .email("test" + i + "@example.com")
                            .password("Password123!@#" + i)
                            .nickname("테스트유저" + i)
                            .userRole(UserRole.ROLE_USER)
                            .build();
                    return userRepository.save(user);
                })
                .collect(Collectors.toList());
    }
}
