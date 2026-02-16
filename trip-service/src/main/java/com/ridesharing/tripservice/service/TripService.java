package com.ridesharing.tripservice.service;

import com.ridesharing.tripservice.model.Trip;
import com.ridesharing.tripservice.model.TripStatus;

import java.util.List;
import java.util.Optional;

public interface TripService {

    Trip createTrip(Trip trip);

    Optional<Trip> getTripById(Long id);

    List<Trip> getAllTrips();

    List<Trip> getTripsByPassenger(Long passengerId);

    List<Trip> getTripsByDriver(Long driverId);

    List<Trip> getTripsByStatus(TripStatus status);

    Trip updateTripStatus(Long tripId, TripStatus status);

    Trip assignDriver(Long tripId, Long driverId);

    void deleteTrip(Long id);
}