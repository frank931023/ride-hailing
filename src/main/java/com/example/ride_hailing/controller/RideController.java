package com.example.ride_hailing.controller;

import com.example.ride_hailing.model.*;
import com.example.ride_hailing.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        String expectedPickUpTime = payload.get("expectedPickUpTime");
        return rideService.createRideRequest(pickUpLocation, destination, expectedPickUpTime);
    }

    @PostMapping("/cancel")
    public Map<String, String> cancelRide() {
        rideService.cancelRideRequest();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ride request cancelled");
        return response;
    }

    @GetMapping("/current")
    public RideRequest getCurrentRideRequest() {
        return rideService.getCurrentRideRequest();
    }

    @GetMapping("/bids")
    public List<Bid> getAllBids() {
        return rideService.getAllBids();
    }

    @GetMapping("/bids/pending")
    public List<Bid> getPendingBids() {
        return rideService.getPendingBids();
    }

    @PostMapping("/bids/submit")
    public Bid submitBid(@RequestBody Map<String, Object> payload) {
        String driverId = (String) payload.get("driverId");
        int price = ((Number) payload.get("price")).intValue();
        return rideService.submitBid(driverId, price);
    }

    @PostMapping("/bids/withdraw")
    public Map<String, String> withdrawBid(@RequestBody Map<String, String> payload) {
        String bidId = payload.get("bidId");
        String driverId = payload.get("driverId");
        rideService.withdrawBid(bidId, driverId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bid withdrawn");
        return response;
    }

    @PostMapping("/bids/select")
    public Map<String, String> selectBid(@RequestBody Map<String, String> payload) {
        String bidId = payload.get("bidId");
        rideService.passengerSelectBid(bidId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bid selected, ride matched!");
        return response;
    }

    @GetMapping("/contact-info")
    public Map<String, String> getContactInfo() {
        String info = rideService.getMatchedContactInfo();
        Map<String, String> response = new HashMap<>();
        response.put("info", info);
        return response;
    }

    @PostMapping("/driver-status")
    public Map<String, String> setDriverStatus(@RequestBody Map<String, Object> payload) {
        String driverId = (String) payload.get("driverId");
        boolean available = (Boolean) payload.get("available");
        rideService.setDriverAvailability(driverId, available);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Driver status updated");
        return response;
    }
}