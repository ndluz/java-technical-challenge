package com.challenge.coupon.controller.api;

import com.challenge.coupon.domain.entity.coupon.Coupon;
import com.challenge.coupon.domain.entity.coupon.CouponStatus;

import java.math.BigDecimal;

public record CouponResponse(
        String id,
        String code,
        String description,
        BigDecimal discountValue,
        String expirationDate,
        CouponStatus status,
        Boolean published,
        Boolean redeemed
) {

    public static CouponResponse from(Coupon coupon) {

       return new CouponResponse(
               coupon.getId().toString(),
               coupon.getCode(),
               coupon.getDescription(),
               coupon.getDiscountValue(),
               coupon.getExpirationDate().toString(),
               coupon.getStatus(),
               coupon.getPublished(),
               coupon.getRedeemed()
       );
    }
}