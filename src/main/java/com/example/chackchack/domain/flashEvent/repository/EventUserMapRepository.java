package com.example.chackchack.domain.flashEvent.repository;

import com.example.chackchack.domain.flashEvent.entity.EventMap;
import com.example.chackchack.domain.flashEvent.entity.EventUserMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventUserMapRepository extends JpaRepository<EventUserMap, Long> {

    int countByEventMap(EventMap eventMap);
}
