package com.tds.assigment.car_parking.controller;

import com.tds.assigment.car_parking.dto.BillResponse;
import com.tds.assigment.car_parking.dto.ExitRequest;
import com.tds.assigment.car_parking.dto.ParkVehicleRequest;
import com.tds.assigment.car_parking.dto.ParkVehicleResponse;
import com.tds.assigment.car_parking.dto.ParkingStatusResponse;
import com.tds.assigment.car_parking.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
@Tag(name = "Car  Parking", description = "Car Parking management API")
public class ParkingController {
    private final ParkingService parkingService;

    @GetMapping
    @Operation(summary = "Get available and occupated spaces")
    public ResponseEntity<ParkingStatusResponse> getStatus() {
        return ResponseEntity.ok(parkingService.getStatus());
    }

    @PostMapping
    @Operation(summary = "Park a vehicle in the first available space")
    public ResponseEntity<ParkVehicleResponse> parkVehicle(@RequestBody ParkVehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingService.parkVehicle(request));
    }

    @PostMapping("/bill")
    @Operation(summary = "Exit vehicle and calculate parking charge")
    public ResponseEntity<BillResponse> exitVehicle(@RequestBody ExitRequest request) {
        return ResponseEntity.ok(parkingService.exitVehicle(request));
    }
}
