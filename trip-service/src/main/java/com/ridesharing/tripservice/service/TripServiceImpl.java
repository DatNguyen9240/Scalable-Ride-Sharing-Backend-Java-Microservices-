package com.ridesharing.tripservice.service;

import com.ridesharing.tripservice.model.Trip;
import com.ridesharing.tripservice.model.TripStatus;
import com.ridesharing.tripservice.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripRepository tripRepository;

    @Override
    public Trip createTrip(Trip trip) {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            throw new RuntimeException("Missing tenant context");
        }
        trip.setTenantId(tenantId);
        trip.setStatus(TripStatus.REQUESTED);
        trip.setRequestedAt(LocalDateTime.now());
        return tripRepository.save(trip);
    }

    @Override
    public Optional<Trip> getTripById(Long id) {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        return tripRepository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public List<Trip> getAllTrips() {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        return tripRepository.findByTenantId(tenantId);
    }

    @Override
    public List<Trip> getTripsByPassenger(Long passengerId) {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        return tripRepository.findByTenantIdAndPassengerId(tenantId, passengerId);
    }

    @Override
    public List<Trip> getTripsByDriver(Long driverId) {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        return tripRepository.findByTenantIdAndDriverId(tenantId, driverId);
    }

    @Override
    public List<Trip> getTripsByStatus(TripStatus status) {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        return tripRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public Trip updateTripStatus(Long tripId, TripStatus status) {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        Optional<Trip> tripOpt = tripRepository.findByIdAndTenantId(tripId, tenantId);
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
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        Optional<Trip> tripOpt = tripRepository.findByIdAndTenantId(tripId, tenantId);
        if (tripOpt.isPresent()) {
            Trip trip = tripOpt.get();
            trip.setDriverId(driverId);
            trip.setStatus(TripStatus.ACCEPTED);
            return tripRepository.save(trip);
        }
        throw new RuntimeException("Trip not found");
    }

    @Override
    @Transactional
    public void deleteTrip(Long id) {
        String tenantId = com.ridesharing.tripservice.util.TenantContext.getTenantId();
        tripRepository.deleteByIdAndTenantId(id, tenantId);
    }
}