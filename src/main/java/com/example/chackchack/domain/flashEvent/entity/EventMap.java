package com.example.chackchack.domain.flashEvent.entity;

import com.example.chackchack.common.entity.BaseEntity;
import com.example.chackchack.domain.flashEvent.dto.request.EventCreateRequest;
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

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "description", nullable = false)
    String description;

    @Column(nullable = false)
    private Integer maxParticipants;

    private EventMap(String title, String description, int maxParticipants) {
        this.title = title;
        this.description = description;
        this.maxParticipants = maxParticipants;
    }

    public static EventMap from(EventCreateRequest createRequest) {
        return new EventMap(
                createRequest.title(),
                createRequest.description(),
                createRequest.maxParticipants()
        );
    }
}
