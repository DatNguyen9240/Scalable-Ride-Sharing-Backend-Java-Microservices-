package com.ridesharing.tripservice.repository;

import com.ridesharing.tripservice.model.Trip;
import com.ridesharing.tripservice.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByTenantId(String tenantId);

    List<Trip> findByTenantIdAndPassengerId(String tenantId, Long passengerId);

    List<Trip> findByTenantIdAndDriverId(String tenantId, Long driverId);

    List<Trip> findByTenantIdAndStatus(String tenantId, TripStatus status);

    java.util.Optional<Trip> findByIdAndTenantId(Long id, String tenantId);

    void deleteByIdAndTenantId(Long id, String tenantId);
} 