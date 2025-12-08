package com.example.ride_hailing.controller;

import com.example.ride_hailing.model.Bid;
import com.example.ride_hailing.model.Driver;
import com.example.ride_hailing.model.RideRequest;
import com.example.ride_hailing.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("/drivers")
    public List<Driver> getAllDrivers() {
        return rideService.getAllDrivers();
    }

    @PostMapping("/request")
    public RideRequest requestRide(@RequestBody Map<String, String> payload) {
        return rideService.createRideRequest(
            payload.get("pickUpLocation"), 
            payload.get("destination"), 
            payload.get("expectedPickUpTime")
        );
    }

    @PostMapping("/cancel")
    public Map<String, String> cancelRide() {
        rideService.cancelRideRequest();
        return Map.of("message", "Ride request cancelled");
    }

    @GetMapping("/current")
    public RideRequest getCurrentRideRequest() {
        return rideService.getCurrentRideRequest();
    }

    @GetMapping("/bids")
    public List<Bid> getAllBids() {
        return rideService.getAllBids();
    }

    @PostMapping("/bids/submit")
    public Bid submitBid(@RequestBody Map<String, Object> payload) {
        String driverId = (String) payload.get("driverId");
        int price = ((Number) payload.get("price")).intValue();
        return rideService.submitBid(driverId, price);
    }

    @PostMapping("/bids/select")
    public Map<String, String> selectBid(@RequestBody Map<String, String> payload) {
        String bidId = payload.get("bidId");
        rideService.passengerSelectBid(bidId);
        return Map.of("message", "Bid selected, ride matched!");
    }

    @GetMapping("/contact-info")
    public Map<String, String> getContactInfo() {
        String info = rideService.getMatchedContactInfo();
        return Map.of("info", info);
    }

    @PostMapping("/driver-status")
    public Map<String, String> setDriverStatus(@RequestBody Map<String, Object> payload) {
        String driverId = (String) payload.get("driverId");
        boolean available = (Boolean) payload.get("available");
        rideService.setDriverAvailability(driverId, available);
        return Map.of("message", "Driver status updated");
    }
}