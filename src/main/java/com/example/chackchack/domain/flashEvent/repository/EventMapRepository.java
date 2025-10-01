package com.example.chackchack.domain.flashEvent.repository;

import com.example.chackchack.domain.flashEvent.entity.EventMap;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventMapRepository extends JpaRepository<EventMap,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EventMap e WHERE e.id = :id")
    Optional<EventMap> findByIdWithDistributedLock(@Param("id") Long id);
}
