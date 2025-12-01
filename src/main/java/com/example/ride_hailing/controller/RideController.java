package com.example.ride_hailing.controller;

import com.example.ride_hailing.model.*;
import com.example.ride_hailing.service.RideService;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private RideService rideService;
    private List<Driver> drivers = new ArrayList<>();

    public RideController() {
        // Initialize with some dummy drivers
        drivers.add(new Driver("1", "John Doe", "1234567890", true));
        drivers.add(new Driver("2", "Jane Smith", "0987654321", true));
        // this.rideService = new RideService(drivers);
    }

    // @PostMapping("/request")
    // public RideRequest requestRide(@RequestBody RideRequest rideRequest) {
    //     Passenger passenger = rideRequest.getPassenger();
    //     return rideService.createRideRequest(passenger, rideRequest.getPickUpLocation(), rideRequest.getDestination());
    // }

    // @PostMapping("/cancel")
    // public void cancelRide(@RequestBody RideRequest rideRequest) {
    //     Passenger passenger = rideRequest.getPassenger();
    //     rideService.cancelRideRequest(passenger, rideRequest);
    // }
}