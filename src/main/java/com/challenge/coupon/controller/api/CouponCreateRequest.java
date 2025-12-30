package com.challenge.coupon.controller.api;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponCreateRequest(
        @NotNull(message = "Coupon code is required")
        String code,
        @NotNull(message = "Description is required")
        String description,
        @NotNull(message = "Discount value is required")
        BigDecimal discountValue,
        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration date must be in the future")
        LocalDateTime expirationDate,
        Boolean published
){}