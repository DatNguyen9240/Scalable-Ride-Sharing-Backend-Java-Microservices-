package com.ridesharing.paymentservice.listener;

import com.ridesharing.paymentservice.events.TripCreatedEvent;
import com.ridesharing.paymentservice.model.Payment;
import com.ridesharing.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TripCreatedListener {

    @Autowired
    private PaymentService paymentService;

    @KafkaListener(topics = "trips.created", groupId = "payment-service")
    public void onTripCreated(TripCreatedEvent event) {
        // mock payment processing for the created trip (POC)
        Payment p = new Payment();
        p.setTripId(event.getTripId());
        p.setUserId(event.getPassengerId());
        p.setAmount(BigDecimal.valueOf(9.99));
        paymentService.createPayment(p);
    }
}
