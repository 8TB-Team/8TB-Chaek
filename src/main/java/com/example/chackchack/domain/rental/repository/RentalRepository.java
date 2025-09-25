package com.example.chackchack.domain.rental.repository;

import com.example.chackchack.domain.rental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
