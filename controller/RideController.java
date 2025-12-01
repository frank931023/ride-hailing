// @PostMapping("/request")
// public List<Driver> requestRide(@RequestBody RideRequest rideRequest) {
//     Passenger passenger = rideRequest.getPassenger();
//     rideRequest.newRide(passenger, rideRequest.getPickUpLocation(), rideRequest.getDestination());
//     List<Driver> availableDrivers = matchService.matchDriver(rideRequest);
//     if (availableDrivers == null || availableDrivers.isEmpty()) {
//         System.out.println("No drivers available.");
//         return new ArrayList<>();
//     }
//     matchService.sendAcceptedDriver(availableDrivers);
//     return availableDrivers;
// }

// @PostMapping("/chooseDriver")
// public RideRequest chooseDriver(@RequestParam String driverId, @RequestBody RideRequest rideRequest) {
//     Driver chosenDriver = drivers.stream()
//                                  .filter(driver -> driver.getId().equals(driverId))
//                                  .findFirst()
//                                  .orElseThrow(() -> new IllegalArgumentException("Driver not found"));

//     rideRequest.setDriver(chosenDriver);
//     chosenDriver.setAvailable(false);
//     rideRequest.updateStatus("Matched");
//     return rideRequest;
// }