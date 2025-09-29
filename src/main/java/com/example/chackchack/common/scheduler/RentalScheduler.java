package com.example.chackchack.common.scheduler;

import com.example.chackchack.domain.rental.entity.Rental;
import com.example.chackchack.domain.rental.enums.RentalStatus;
import com.example.chackchack.domain.rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RentalScheduler {
    private final RentalRepository rentalRepository;

    @Scheduled(cron = "0 59 23 * * *")
    public void updateOverdueRentals() {
        log.info("[RentalScheduler] 연체 상태 업데이트 시작 - {}", LocalDateTime.now());

        // today 포함해서 조회
        List<Rental> overdueRentals = rentalRepository
                .findAllByStatusAndDueDateBeforeOrEqual(RentalStatus.RENTED, LocalDate.now());

        if (overdueRentals.isEmpty()) {
            log.info("[RentalScheduler] 연체 대상 없음");
            return;
        }

        // 상태 변경
        overdueRentals.forEach(Rental::markOverdue);

        // DB 반영
        rentalRepository.saveAll(overdueRentals);
        rentalRepository.flush(); // 즉시 반영

        log.info("[RentalScheduler] 연체 상태 처리 완료 - {}건", overdueRentals.size());
    }
}

