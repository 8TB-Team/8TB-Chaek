package com.example.chackchack.domain.reservation.repository;

import com.example.chackchack.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
