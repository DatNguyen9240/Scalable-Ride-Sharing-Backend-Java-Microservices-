package com.ridesharing.tripservice.repository;

import com.ridesharing.tripservice.model.Trip;
import com.ridesharing.tripservice.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByPassengerId(Long passengerId);

    List<Trip> findByDriverId(Long driverId);

    List<Trip> findByStatus(TripStatus status);
}