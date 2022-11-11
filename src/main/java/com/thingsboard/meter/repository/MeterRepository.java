package com.thingsboard.meter.repository;

import com.thingsboard.meter.model.Meter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author @bkalika
 */
public interface MeterRepository extends JpaRepository<Meter, Long> {
    List<Meter> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}
