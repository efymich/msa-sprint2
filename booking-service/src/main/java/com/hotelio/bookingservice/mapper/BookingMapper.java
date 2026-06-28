package com.hotelio.bookingservice.mapper;

import com.hotelio.bookingservice.entity.Booking;
import com.hotelio.core.middleware.BookingCreatedEvent;
import com.hotelio.proto.booking.BookingRequest;
import com.hotelio.proto.booking.BookingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponse pojoToProto(Booking booking);

    Booking protoToPojo(BookingRequest bookingRequest);

    BookingCreatedEvent pojoToEvent(Booking booking);
}
