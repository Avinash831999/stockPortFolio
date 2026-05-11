package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector,Long> {
    boolean existsByName(String name);
}
