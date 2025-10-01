package com.example.chackchack.domain.flashEvent.entity;

import com.example.chackchack.common.entity.SoftDeleteEntity;
import com.example.chackchack.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_map_id"})
})
public class EventUserMap extends SoftDeleteEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_map_id")
    private EventMap eventMap;

    private EventUserMap(User user, EventMap eventMap) {
        this.user = user;
        this.eventMap = eventMap;
    }

    public static EventUserMap of(User user, EventMap eventMap) {
        return new EventUserMap(user, eventMap);
    }
}
