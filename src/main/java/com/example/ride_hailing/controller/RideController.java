package com.example.ride_hailing.controller;

import com.example.ride_hailing.model.*;
import com.example.ride_hailing.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    @GetMapping("/drivers")
    public List<Driver> getAllDrivers() {
        return rideService.getAllDrivers();
    }

    @PostMapping("/request")
    public RideRequest requestRide(@RequestBody Map<String, String> payload) {
        String pickUpLocation = payload.get("pickUpLocation");
        String destination = payload.get("destination");
        return rideService.createRideRequest(pickUpLocation, destination);
    }

    @PostMapping("/cancel")
    public void cancelRide() {
        rideService.cancelRideRequest();
    }

    @GetMapping("/current")
    public RideRequest getCurrentRideRequest() {
        return rideService.getCurrentRideRequest();
    }

    @GetMapping("/available-drivers")
    public List<Driver> getAvailableDrivers() {
        return rideService.getAvailableDrivers();
    }

    @PostMapping("/choose-driver")
    public void chooseDriver(@RequestBody Map<String, String> payload) {
        String driverName = payload.get("driverName");
        rideService.passengerChooseDriver(driverName);
    }

    @PostMapping("/driver-confirm")
    public boolean driverConfirm(@RequestBody Map<String, Object> payload) {
        String driverName = (String) payload.get("driverName");
        boolean confirm = (Boolean) payload.get("confirm");
        return rideService.driverConfirmRide(driverName, confirm);
    }

    @PostMapping("/driver-status")
    public void setDriverStatus(@RequestBody Map<String, Object> payload) {
        String driverName = (String) payload.get("driverName");
        boolean available = (Boolean) payload.get("available");
        rideService.setDriverAvailability(driverName, available);
    }
}