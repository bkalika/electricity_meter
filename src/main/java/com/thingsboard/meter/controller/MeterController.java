package com.thingsboard.meter.controller;

import com.thingsboard.meter.model.Meter;
import com.thingsboard.meter.service.IMeterService;
import com.thingsboard.meter.service.MeterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author @bkalika
 */
@RestController
@RequestMapping(path="api/v1/meters")
public class MeterController {
    private final IMeterService meterService;

    @Autowired
    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @GetMapping("/group-by-hours")
    public ResponseEntity<Map<Object, BigDecimal>> getMetersByHours(
            @RequestParam(value="startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime startDate,

            @RequestParam(value="endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime endDate) {

        List<Meter> meters  = meterService.findByTimestampBetween(startDate, endDate);

        Map<Object, BigDecimal> hoursRange = meterService.sumValuesByHour(meters, startDate, endDate);
        return new ResponseEntity<>(hoursRange, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Meter>> getMetersByTimeStampBetween(
            @RequestParam(value="startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime startDate,

            @RequestParam(value="endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime endDate) {

        List<Meter> sortedMeters;

        if(startDate != null && endDate != null) {
            sortedMeters = meterService.findByTimestampBetween(startDate, endDate);
        } else {
            sortedMeters = meterService.getMeters();
        }

        return new ResponseEntity<>(sortedMeters, HttpStatus.OK);
    }

    @GetMapping("/sum-two-meters")
    public ResponseEntity<Map<String, BigDecimal>> getValueSumByTimestampBetween(
            @RequestParam(value="startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime startDate,

            @RequestParam(value="endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime endDate) {

        BigDecimal sum = meterService.getSumBetweenTwoTimestamp(
                meterService.findByTimestampBetween(startDate, endDate));

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("sum", sum);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Meter> createMeter(@RequestBody Meter meter) {
        Meter newMeter = meterService.create(meter);
        return ResponseEntity.ok().body(newMeter);
    }
}
