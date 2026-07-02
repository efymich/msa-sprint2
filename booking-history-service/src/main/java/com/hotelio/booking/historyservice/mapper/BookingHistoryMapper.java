package com.hotelio.booking.historyservice.mapper;

import com.hotelio.booking.historyservice.entity.BookingHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingHistoryMapper {

    BookingHistory eventToEntity(com.hotelio.core.middleware.BookingCreatedEvent event);
}
