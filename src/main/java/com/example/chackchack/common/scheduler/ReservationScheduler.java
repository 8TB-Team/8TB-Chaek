package com.example.chackchack.common.scheduler;

import com.example.chackchack.domain.reservation.service.ReservationInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationInternalService reservationInternalService;

    /**
     * 10분 단위로 만료된 예약처리 실행
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void processExpiredReservation() {
        log.info("=== 예약 만료 처리 시작 ===");
        try{
            reservationInternalService.processExpiredReservation();
            log.info("예약 만료 처리 완료");
        }catch (Exception e){
            log.error("예약 만료 처리 과정 중 문제 발생", e);
        }
    }
}
