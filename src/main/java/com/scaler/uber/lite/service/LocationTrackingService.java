package com.scaler.uber.lite.service;

import org.springframework.stereotype.Service;

// deployed as a microservice
// consumes the location updates from a queue
// service (behind a REST API perhaps) that can provide drivers near a location

@Service
public class LocationTrackingService {
//    @Autowired
//    MessageQueue messageQueue;
//    @Autowired
//    Constants constants;
//    @Autowired
//    DriverRepository driverRepository;
//
//    QuadTree world = new QuadTree();
//
//    public List<Driver> getDriversNearLocation(ExactLocation pickup) {
//        return world.findNeighboursIds(
//                pickup.getLatitude(),
//                pickup.getLongitude(),
//                constants.getMaxDistanceKmForDriverMatching())
//                .stream()
//                .map(driverId -> driverRepository.findById(driverId).orElseThrow(() ->
//                        new InvalidDriverException("No Driver found")))
//                .collect(Collectors.toList());
//    }
//
//    public void updateDriverLocation(Driver driver, ExactLocation location) {
//        world.removeNeighbour(driver.getId()); // if the driver is not in the world, it won't throw an error
//        world.addNeighbour(driver.getId(), location.getLatitude(), location.getLongitude());
//        driver.setLastKnownLocation(location);
//        driverRepository.save(driver);
//    }
//
//    @Scheduled(fixedRate = 1000)
//    public void consumer() {
//        MQMessage m = messageQueue.consumeMessage(constants.getDriverMatchingTopicName());
//        if (m == null) return;
//        Message message = (Message) m;
//        updateDriverLocation(message.getDriver(), message.getLocation());
//    }
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    public static class Message implements MQMessage {
//        private Driver driver;
//        private ExactLocation location;
//    }
}
