package com.ridesharing.tripservice.controller;

import com.ridesharing.tripservice.model.Trip;
import com.ridesharing.tripservice.model.TripStatus;
import com.ridesharing.tripservice.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

@GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.getAllTrips();
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody Trip trip) {
        try {
            Trip createdTrip = tripService.createTrip(trip);
            return new ResponseEntity<>(createdTrip, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long id) {
        Optional<Trip> trip = tripService.getTripById(id);
        return trip.map(t -> new ResponseEntity<>(t, HttpStatus.OK))
                   .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<Trip>> getTripsByPassenger(@PathVariable Long passengerId) {
        List<Trip> trips = tripService.getTripsByPassenger(passengerId);
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Trip>> getTripsByDriver(@PathVariable Long driverId) {
        List<Trip> trips = tripService.getTripsByDriver(driverId);
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Trip>> getTripsByStatus(@PathVariable TripStatus status) {
        List<Trip> trips = tripService.getTripsByStatus(status);
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Trip> updateTripStatus(@PathVariable Long id, @RequestParam TripStatus status) {
        try {
            Trip updatedTrip = tripService.updateTripStatus(id, status);
            return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/assign-driver")
    public ResponseEntity<Trip> assignDriver(@PathVariable Long id, @RequestParam Long driverId) {
        try {
            Trip updatedTrip = tripService.assignDriver(id, driverId);
            return new ResponseEntity<>(updatedTrip, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}