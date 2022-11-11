package com.thingsboard.meter.controller;

import com.thingsboard.meter.model.Meter;
import com.thingsboard.meter.service.MeterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author @bkalika
 */
@RestController
@RequestMapping(path="api/v1/meters")
public class MeterController {
    private final MeterService meterService;

    @Autowired
    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @GetMapping("/sum-two-meters")
    public ResponseEntity<String> getValueSumByTimestampBetween(
            @RequestParam(value="startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime startDate,

            @RequestParam(value="endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime endDate) {

        BigDecimal sum = meterService.getSumBetweenTwoTimestamp(
                meterService.findByTimestampBetween(startDate, endDate));
        JSONObject result = new JSONObject();
        result.put("sum", sum);

        return ResponseEntity.ok(result.toString());
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

    @PostMapping()
    public ResponseEntity<Meter> createMeter(@RequestBody Meter meter) {
        Meter newMeter = meterService.create(meter);
        return ResponseEntity.ok().body(newMeter);
    }
}
