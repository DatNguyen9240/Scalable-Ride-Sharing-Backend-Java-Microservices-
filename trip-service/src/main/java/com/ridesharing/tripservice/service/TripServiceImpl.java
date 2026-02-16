package com.ridesharing.tripservice.service;

import com.ridesharing.tripservice.model.Trip;
import com.ridesharing.tripservice.model.TripStatus;
import com.ridesharing.tripservice.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Override
    public Trip createTrip(Trip trip) {
        trip.setStatus(TripStatus.REQUESTED);
        trip.setRequestedAt(LocalDateTime.now());
        return tripRepository.save(trip);
    }

    @Override
    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public List<Trip> getTripsByPassenger(Long passengerId) {
        return tripRepository.findByPassengerId(passengerId);
    }

    @Override
    public List<Trip> getTripsByDriver(Long driverId) {
        return tripRepository.findByDriverId(driverId);
    }

    @Override
    public List<Trip> getTripsByStatus(TripStatus status) {
        return tripRepository.findByStatus(status);
    }

    @Override
    public Trip updateTripStatus(Long tripId, TripStatus status) {
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isPresent()) {
            Trip trip = tripOpt.get();
            trip.setStatus(status);

            if (status == TripStatus.IN_PROGRESS && trip.getStartedAt() == null) {
                trip.setStartedAt(LocalDateTime.now());
            } else if (status == TripStatus.COMPLETED && trip.getCompletedAt() == null) {
                trip.setCompletedAt(LocalDateTime.now());
            }

            return tripRepository.save(trip);
        }
        throw new RuntimeException("Trip not found");
    }

    @Override
    public Trip assignDriver(Long tripId, Long driverId) {
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isPresent()) {
            Trip trip = tripOpt.get();
            trip.setDriverId(driverId);
            trip.setStatus(TripStatus.ACCEPTED);
            return tripRepository.save(trip);
        }
        throw new RuntimeException("Trip not found");
    }

    @Override
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}