package com.challenge.coupon.repository;

import com.challenge.coupon.domain.entity.coupon.Coupon;
import com.challenge.coupon.domain.entity.coupon.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    Boolean existsByCodeAndStatus(String code, CouponStatus status);
}
