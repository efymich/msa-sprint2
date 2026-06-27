package com.hotelio.bookingservice.client;

import com.hotelio.bookingservice.entity.PromoCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "promocodeservice", url = "${monolith.promocodeservice.url}")
public interface MonolithPromoCodeServiceClient {

    @GetMapping("/validate")
    PromoCode validate(@RequestParam String promoCode, @RequestParam String userId);
}
