package com.hotelio.bookingservice.mapper;

import com.hotelio.bookingservice.entity.Booking;
import com.hotelio.core.middleware.BookingCreatedEvent;
import com.hotelio.proto.booking.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponse pojoToProto(Booking booking);

    @Mapping(source = "id", target = "bookingId")
    BookingCreatedEvent pojoToEvent(Booking booking);

    default String map(Instant instant) {
        return (instant == null ? Instant.now() : DateTimeFormatter.ISO_INSTANT.format(instant)).toString();
    }
}
