package com.tds.assigment.car_parking.service;

import com.tds.assigment.car_parking.dto.BillResponse;
import com.tds.assigment.car_parking.dto.ExitRequest;
import com.tds.assigment.car_parking.dto.ParkVehicleRequest;
import com.tds.assigment.car_parking.dto.ParkVehicleResponse;
import com.tds.assigment.car_parking.dto.ParkingStatusResponse;
import com.tds.assigment.car_parking.model.ParkingSpace;
import com.tds.assigment.car_parking.model.VehicleType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingService {
    private static final int TOTAL_SPACES = 10;
    private static final double ADDITIONAL_CHARGE_PER_5_MINUTES = 1.0;

    private final List<ParkingSpace> spaces = new ArrayList<>();

    @PostConstruct
    public void init() {
        for (int i = 1; i <= TOTAL_SPACES; i++) {
            spaces.add(new ParkingSpace(i));
        }
    }

    public ParkingStatusResponse getStatus() {
        long occupied = spaces.stream()
                .filter(ParkingSpace::isOccupied).count();
        log.debug("Status - checked: occupied: {}, available: {}", occupied, TOTAL_SPACES - occupied);

        return new ParkingStatusResponse((int) (TOTAL_SPACES - occupied), (int) occupied);
    }

    public ParkVehicleResponse parkVehicle(ParkVehicleRequest request) {
        ParkingSpace space = spaces.stream()
                .filter(s -> !s.isOccupied()).findFirst()
                .orElseThrow(() -> new IllegalStateException("No available spaces"));

        VehicleType vehicleType = VehicleType.fromCode(request.vehicleType());
        LocalDateTime timeIn = LocalDateTime.now();
        space.occupy(request.vehicleReg(), vehicleType, timeIn);

        log.info("Vehicle {} parked on space {}", request.vehicleReg(), space.getSpaceNumber());
        return new ParkVehicleResponse(request.vehicleReg(), space.getSpaceNumber(), timeIn);
    }

    public BillResponse exitVehicle(ExitRequest request) {
        ParkingSpace space = spaces.stream()
                .filter(s -> s.isOccupied() && s.getVehicleReg().equals(request.vehicleReg()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + request.vehicleReg()));

        LocalDateTime timeOut = LocalDateTime.now();
        double charge = calculateCharge(space, timeOut);

        String billId = UUID.randomUUID().toString();

        BillResponse bill = new BillResponse(billId, space.getVehicleReg(), charge, space.getTimeIn(), timeOut);

        log.info("Vehicle {} exited, charge: {}", request.vehicleReg(), charge);
        space.vacate();

        return bill;

    }

    private double calculateCharge(ParkingSpace space, LocalDateTime timeOut) {
        long minutes = ChronoUnit.MINUTES.between(space.getTimeIn(), timeOut);
        double baseCharge = minutes * space.getVehicleType().getRatePerMinute();
        double additionalCharge = Math.floor(minutes / 5.0) * ADDITIONAL_CHARGE_PER_5_MINUTES;

        return Math.round((baseCharge + additionalCharge) * 100.0) / 100.0;

    }
}
