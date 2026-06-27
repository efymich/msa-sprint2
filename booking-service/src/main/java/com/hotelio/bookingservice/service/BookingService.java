package com.hotelio.bookingservice.service;

import com.hotelio.bookingservice.client.MonolithHotelServiceClient;
import com.hotelio.bookingservice.client.MonolithPromoCodeServiceClient;
import com.hotelio.bookingservice.client.MonolithReviewServiceClient;
import com.hotelio.bookingservice.client.MonolithUserServiceClient;
import com.hotelio.bookingservice.entity.Booking;
import com.hotelio.bookingservice.entity.PromoCode;
import com.hotelio.bookingservice.mapper.BookingMapper;
import com.hotelio.bookingservice.repository.BookingRepository;
import com.hotelio.proto.booking.BookingRequest;
import com.hotelio.proto.booking.BookingResponse;
import com.hotelio.proto.booking.BookingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@GrpcService
public class BookingService extends BookingServiceGrpc.BookingServiceImplBase {

    private final BookingRepository bookingRepository;

    private final MonolithUserServiceClient userServiceClient;

    private final MonolithHotelServiceClient hotelServiceClient;

    private final MonolithReviewServiceClient reviewServiceClient;

    private final MonolithPromoCodeServiceClient promoCodeServiceClient;

    private final BookingMapper bookingMapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, RestTemplate restTemplate, MonolithUserServiceClient userServiceClient, MonolithHotelServiceClient hotelServiceClient, MonolithReviewServiceClient reviewServiceClient, MonolithPromoCodeServiceClient promoCodeServiceClient, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userServiceClient = userServiceClient;
        this.hotelServiceClient = hotelServiceClient;
        this.reviewServiceClient = reviewServiceClient;
        this.promoCodeServiceClient = promoCodeServiceClient;
        this.bookingMapper = bookingMapper;
    }


    @Override
    public void createBooking(BookingRequest request, StreamObserver<BookingResponse> responseObserver) {

        String userId = request.getUserId();
        String hotelId = request.getHotelId();
        String promoCode = request.getPromoCode();

        validateUser(userId);
        validateHotel(hotelId);

        double basePrice = resolveBasePrice(userId);
        double discount = resolvePromoDiscount(promoCode, userId);

        double finalPrice = basePrice - discount;
        log.info("Final price calculated: base={}, discount={}, final={}", basePrice, discount, finalPrice);


        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setHotelId(hotelId);
        booking.setPromoCode(promoCode);
        booking.setDiscountPercent(discount);
        booking.setPrice(finalPrice);

        bookingRepository.save(booking);

        BookingResponse bookingResponse = bookingMapper.pojoToProto(booking);

        responseObserver.onNext(bookingResponse);
        responseObserver.onCompleted();
    }

    private void validateUser(String userId) {
        if (!userServiceClient.isUserActive(userId)) {
            log.warn("User {} is inactive", userId);
            throw new IllegalArgumentException("User is inactive");
        }
        if (userServiceClient.isUserBlacklisted(userId)) {
            log.warn("User {} is blacklisted", userId);
            throw new IllegalArgumentException("User is blacklisted");
        }
    }

    private void validateHotel(String hotelId) {
        if (!hotelServiceClient.isHotelOperational(hotelId)) {
            log.warn("Hotel {} is not operational", hotelId);
            throw new IllegalArgumentException("Hotel is not operational");
        }
        if (!reviewServiceClient.isTrustedHotel(hotelId)) {
            log.warn("Hotel {} is not trusted", hotelId);
            throw new IllegalArgumentException("Hotel is not trusted based on reviews");
        }
        if (hotelServiceClient.isHotelFullyBooked(hotelId)) {
            log.warn("Hotel {} is fully booked", hotelId);
            throw new IllegalArgumentException("Hotel is fully booked");
        }
    }

    private double resolveBasePrice(String userId) {
        Optional<String> statusOpt = Optional.of(userServiceClient.getUserStatus(userId).toString());
        return statusOpt.map(status -> {
            boolean isVip = status.equalsIgnoreCase("VIP");
            log.debug("User {} has status '{}', base price is {}", userId, status, isVip ? 80.0 : 100.0);
            return isVip ? 80.0 : 100.0;
        }).orElseGet(() -> {
            log.debug("User {} has unknown status, default base price 100.0", userId);
            return 100.0;
        });
    }

    private double resolvePromoDiscount(String promoCode, String userId) {
        if (promoCode == null) return 0.0;

        PromoCode promo = promoCodeServiceClient.validate(promoCode, userId);
        if (promo == null) {
            log.info("Promo code '{}' is invalid or not applicable for user {}", promoCode, userId);
            return 0.0;
        }

        log.debug("Promo code '{}' applied with discount {}", promoCode, promo.getDiscount());
        return promo.getDiscount();
    }
}

