package com.tds.assigment.car_parking.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ParkingSpace {
    private final int spaceNumber;
    private String vehicleReg;
    private VehicleType vehicleType;
    private LocalDateTime timeIn;

    public boolean isOccupied() {
        return vehicleReg != null;
    }

    public void occupy(String vehicleReg, VehicleType vehicleType, LocalDateTime timeIn) {
        this.vehicleReg = vehicleReg;
        this.vehicleType = vehicleType;
        this.timeIn = timeIn;
    }

    public void vacate() {
        this.vehicleReg = null;
        this.vehicleType = null;
        this.timeIn = null;
    }

}
