package com.tds.assigment.car_parking.service;

import com.tds.assigment.car_parking.dto.ExitRequest;
import com.tds.assigment.car_parking.dto.ParkVehicleRequest;
import com.tds.assigment.car_parking.dto.ParkVehicleResponse;
import com.tds.assigment.car_parking.dto.ParkingStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ParkingServiceTest {
    private ParkingService service;

    @BeforeEach
    void setUp() {
        service = new ParkingService();
        service.init();
    }

    @Test
    void shouldReturn10AvailableSpacesInitially() {
        ParkingStatusResponse status = service.getStatus();
        assertThat(status.availableSpaces()).isEqualTo(10);
        assertThat(status.occupiedSpaces()).isEqualTo(0);
    }

    @Test
    void shouldParkVehicleAndReduceAvailableSpaces() {
        service.parkVehicle(new ParkVehicleRequest("ABC123", 1));

        ParkingStatusResponse status = service.getStatus();
        assertThat(status.availableSpaces()).isEqualTo(9);
        assertThat(status.occupiedSpaces()).isEqualTo(1);
    }

    @Test
    void shouldAssignFirstAvailableSpace() {
        ParkVehicleResponse response = service.parkVehicle(new ParkVehicleRequest("ABC123", 1));
        assertThat(response.SpaceNumber()).isEqualTo(1);
    }

    @Test
    void shouldThrownWhenNoAvailableSpaces() {
        for (int i = 0; i < 10; i++) {
            service.parkVehicle(new ParkVehicleRequest("REG" + i, 1));
        }

        assertThatThrownBy(() -> service.parkVehicle(new ParkVehicleRequest("VIP 100", 1)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No available spaces");
    }

    @Test
    void shouldThrownWhenVehicleNotFound() {
        assertThatThrownBy(() -> service.exitVehicle(new ExitRequest("NOTFOUND 1")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vehicle not found");
    }

    @Test
    void shouldFreeSpaceOnExit() {
        service.parkVehicle(new ParkVehicleRequest("ABC123", 1));
        service.exitVehicle(new ExitRequest("ABC123"));

        ParkingStatusResponse status = service.getStatus();
        assertThat(status.availableSpaces()).isEqualTo(10);
    }

    @Test
    void shouldThrownOnInvalidVehicleType() {
        assertThatThrownBy(() -> service.parkVehicle(new ParkVehicleRequest("ABC123", 4)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid vehicle type");
    }


}