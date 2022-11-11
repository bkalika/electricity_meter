package com.thingsboard.meter.service;

import com.thingsboard.meter.model.Meter;
import com.thingsboard.meter.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * @author @bkalika
 */
@Service
public class MeterService implements IMeterService, Serializable {
    private final MeterRepository meterRepository;

    @Autowired
    public MeterService(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    @Override
    public List<Meter> getMeters() {
        return meterRepository.findAll();
    }

    @Override
    public List<Meter> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<Meter> meterList = meterRepository.findByTimestampBetween(startDate, endDate);
        meterList.sort(Comparator.comparing(Meter::getId));
        return meterList;
    }

    @Override
    public Meter create(Meter meter) {
        return meterRepository.save(meter);
    }

    @Override
    public BigDecimal getSumBetweenTwoTimestamp(List<Meter> meterList) {
        if(meterList.size() == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(meterList.get(meterList.size()-1).getValue() - meterList.get(0).getValue())
                .setScale(2, RoundingMode.CEILING);
    }

}
