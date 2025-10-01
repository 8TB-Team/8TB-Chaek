package com.example.chackchack.domain.flashEvent.entity;

import com.example.chackchack.common.entity.BaseEntity;
import com.example.chackchack.domain.flashEvent.dto.request.EventCreateRequest;
import com.example.chackchack.domain.flashEvent.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventMap extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private EventStatus status;

    public void updateStatus(EventStatus status) {
        this.status = status;
    }

    /** ---------- private 생성자 및 정적 팩토리 메서드 구현 ---------- **/
    private EventMap(String title, String description, int maxParticipants) {
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.status = EventStatus.CREATED;
    }

    public static EventMap from(EventCreateRequest createRequest) {
        return new EventMap(
                createRequest.title(),
                createRequest.description(),
                createRequest.maxParticipants()
        );
    }
}
