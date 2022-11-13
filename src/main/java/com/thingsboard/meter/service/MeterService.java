package com.thingsboard.meter.service;

import com.thingsboard.meter.model.Meter;
import com.thingsboard.meter.repository.IMeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author @bkalika
 */
@Service
public class MeterService implements IMeterService, Serializable {
    private final IMeterRepository meterRepository;

    @Autowired
    public MeterService(IMeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    @Override
    public List<Meter> getMeters() {
        return meterRepository.findAll();
    }

    @Override
    public Map<String, BigDecimal> sumValuesByHour(List<Meter> meters, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, List<Meter>> groupByHour = meters.stream().collect(
                Collectors.groupingBy(time -> time.getTimestamp().truncatedTo(ChronoUnit.HOURS).toString()));

        Map<String, BigDecimal> hoursRange = new TreeMap<>();
        for(; !startDate.isAfter(endDate); startDate = startDate.truncatedTo(ChronoUnit.HOURS).plusHours(1)) {
            hoursRange.put(String.valueOf(startDate.truncatedTo(ChronoUnit.HOURS)), BigDecimal.ZERO);
        }

        for (Map.Entry<String, List<Meter>> entry : groupByHour.entrySet()) {
            String myKey = entry.getKey();
            List<Meter> myList =  entry.getValue();
            hoursRange.put(myKey,
                    BigDecimal.valueOf(myList.get(myList.size()-1).getValue() - myList.get(0).getValue())
                            .setScale(2, RoundingMode.CEILING));
        }

        return hoursRange;
    }

    @Override
    public List<Meter> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<Meter> meterList = meterRepository.findByTimestampBetween(startDate, endDate);
        meterList.sort(Comparator.comparing(Meter::getId));
        return meterList;
    }

    @Override
    public BigDecimal getSumBetweenTwoTimestamp(List<Meter> meterList) {
        if(meterList.size() == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(meterList.get(meterList.size()-1).getValue() - meterList.get(0).getValue())
                .setScale(2, RoundingMode.CEILING);
    }

    @Override
    public Meter create(Meter meter) {
        return meterRepository.save(meter);
    }
}
