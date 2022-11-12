package com.thingsboard.meter.service;

import com.thingsboard.meter.model.Meter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author @bkalika
 */
@Component
public interface IMeterService {
    List<Meter> getMeters();
    List<Meter> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    Meter create(Meter meter);
    BigDecimal getSumBetweenTwoTimestamp(List<Meter> meterList);
    Map<Object, BigDecimal> sumValuesByHour(List<Meter> meters, LocalDateTime startDate, LocalDateTime endDate);
}
