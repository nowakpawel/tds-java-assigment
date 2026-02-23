package com.tds.assigment.car_parking.dto;

import java.time.LocalDateTime;

public record ParkVehicleResponse(String vehicleReg, int SpaceNumber, LocalDateTime timeIn) {
}
