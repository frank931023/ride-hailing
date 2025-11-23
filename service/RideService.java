package com.example.ride_hailing.service;

import com.example.ride_hailing.model.*;
import java.util.List;
import java.util.UUID;

public class RideService {

    private MatchService matchService;

    public RideService(List<Driver> drivers) {
        this.matchService = new MatchService(drivers);
    }

    public RideRequest createRideRequest(Passenger passenger, String pickUpLocation, String destination) {
        RideRequest rideRequest = passenger.requestRide(pickUpLocation, destination);
        Driver matchedDriver = matchService.matchDriver(rideRequest);
        if (matchedDriver != null) {
            matchService.notifyPassenger(rideRequest);
            matchService.notifyDriver(rideRequest);
        }
        return rideRequest;
    }

    public void cancelRideRequest(Passenger passenger, RideRequest rideRequest) {
        passenger.cancelRide(rideRequest);
    }
}