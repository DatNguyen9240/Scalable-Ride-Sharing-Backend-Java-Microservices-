package com.ridesharing.paymentservice.events;

public class TripCreatedEvent {
    private String tenantId;
    private Long tripId;
    private Long passengerId;
    private String pickupLocation;
    private String destination;
    private String requestedAt;

    public TripCreatedEvent() {}

    public TripCreatedEvent(String tenantId, Long tripId, Long passengerId, String pickupLocation, String destination, String requestedAt) {
        this.tenantId = tenantId;
        this.tripId = tripId;
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.requestedAt = requestedAt;
    }

    public String getTenantId() { return tenantId; }
    public Long getTripId() { return tripId; }
    public Long getPassengerId() { return passengerId; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDestination() { return destination; }
    public String getRequestedAt() { return requestedAt; }

    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setRequestedAt(String requestedAt) { this.requestedAt = requestedAt; }
}
