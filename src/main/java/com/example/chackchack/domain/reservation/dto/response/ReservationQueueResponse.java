package com.example.chackchack.domain.reservation.dto.response;

import com.example.chackchack.common.entity.BaseEntity;
import com.example.chackchack.domain.bookItem.entity.BookItem;
import com.example.chackchack.domain.reservation.entity.Reservation;
import com.example.chackchack.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 대기열 조회 응답 DTO
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationQueueResponse {
    private Long bookItemId;
    private Integer totalReaservationWaiting;
    private List<QueueItem> queue;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class QueueItem {
        private Long id;
        private Long userid;
        private Integer priority;
        private Integer queuePositon;
        private LocalDateTime createdAt;

        public static QueueItem of(Reservation reservation, Integer queuePositon) {
            return new QueueItem(
                    reservation.getId(),
                    reservation.getUser().getId(),
                    reservation.getPriority(),
                    queuePositon,
                    reservation.getCreatedAt()
            );
        }
    }
    public static ReservationQueueResponse of(Long bookItemId, Integer totalReaservationWaiting, List<QueueItem> queue) {
        return new ReservationQueueResponse(bookItemId, totalReaservationWaiting, queue);
    }
}
