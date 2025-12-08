package com.example.ride_hailing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RideRequest {

    private String id;
    private Passenger passenger;
    private String pickUpLocation;
    private String destination;
    private String expectedPickUpTime;
    private RequestStatus status;
    private List<Bid> bids;
    private Bid selectedBid;

    public RideRequest() {
        // Default constructor for creating an empty RideRequest instance
        this.bids = new ArrayList<>();
    }

    public RideRequest(Passenger passenger, String pickUpLocation, String destination, String expectedPickUpTime) {
        this.id = UUID.randomUUID().toString();
        this.passenger = passenger;
        this.pickUpLocation = pickUpLocation;
        this.destination = destination;
        this.expectedPickUpTime = expectedPickUpTime;
        this.status = RequestStatus.INITIATE;
        this.bids = new ArrayList<>();
        this.selectedBid = null;
        System.out.println("New ride created: " + id);
    }

    public String getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDestination() {
        return destination;
    }

    public String getExpectedPickUpTime() {
        return expectedPickUpTime;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public List<Bid> getPendingBids() {
        List<Bid> pendingBids = new ArrayList<>();
        for (Bid bid : bids) {
            if (bid.isPending()) {
                pendingBids.add(bid);
            }
        }
        return pendingBids;
    }

    public Bid getSelectedBid() {
        return selectedBid;
    }

    public void addBid(Bid bid) {
        if (this.status != RequestStatus.INITIATE) {
            System.out.println("Cannot add bid. RideRequest status is not INITIATE.");
            return;
        }
        this.bids.add(bid);
        System.out.println("Bid added to RideRequest " + id + " from driver " + bid.getDriverId());
    }

    public void updateStatus(RequestStatus newStatus) {
        if (newStatus == null) {
            System.out.println("Invalid status.");
            return;
        }
        this.status = newStatus;
        System.out.println("Ride status updated to: " + status);
    }

    public void selectBid(Bid bid) {
        if (this.status != RequestStatus.INITIATE) {
            System.out.println("Cannot select bid. RideRequest is not in INITIATE status.");
            return;
        }
        if (!this.bids.contains(bid) || !bid.isPending()) {
            System.out.println("Invalid bid selection.");
            return;
        }
        this.selectedBid = bid;
        this.status = RequestStatus.MATCHED;
        System.out.println("Bid selected and ride matched: " + bid.getId());
    }

    public void cancel() {
        if (this.status == RequestStatus.INITIATE) {
            this.status = RequestStatus.CANCELLED;
            System.out.println("RideRequest " + id + " has been cancelled.");
        } else {
            System.out.println("Cannot cancel RideRequest. Current status: " + status);
        }
    }
}
