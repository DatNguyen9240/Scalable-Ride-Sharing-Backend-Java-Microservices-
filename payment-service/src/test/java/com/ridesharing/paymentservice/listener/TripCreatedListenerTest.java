package com.ridesharing.paymentservice.listener;

import com.ridesharing.paymentservice.events.TripCreatedEvent;
import com.ridesharing.paymentservice.model.Payment;
import com.ridesharing.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TripCreatedListenerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private TripCreatedListener listener;

    @Test
    void onTripCreated_callsPaymentService() {
        TripCreatedEvent evt = new TripCreatedEvent("default", 123L, 7L, "A", "B", "2026-01-01T00:00:00Z");

        listener.onTripCreated(evt);

        verify(paymentService).createPayment(argThat((ArgumentMatcher<Payment>) p ->
                p.getTripId().equals(123L) && p.getUserId().equals(7L)
        ));
    }
}
