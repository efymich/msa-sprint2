package com.hotelio.booking.historyservice.handler;

import com.hotelio.core.middleware.BookingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(topics = "booking-history-topic")
public class BookingCreatedEventHandler {

    @KafkaHandler
    public void handle(BookingCreatedEvent bookingCreatedEvent) {
        log.info("Received a new event: " + bookingCreatedEvent);
    }
}
