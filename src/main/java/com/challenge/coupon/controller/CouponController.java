package com.challenge.coupon.controller;

import com.challenge.coupon.controller.api.CouponCreateRequest;
import com.challenge.coupon.controller.api.CouponResponse;
import com.challenge.coupon.domain.entity.coupon.Coupon;
import com.challenge.coupon.service.coupon.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {

        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<CouponResponse> create(@RequestBody @Valid CouponCreateRequest request) {

        var couponCreated = couponService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CouponResponse.from(couponCreated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {

        couponService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> findById(@PathVariable("id") String id) {

        Coupon coupon = couponService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(CouponResponse.from(coupon));
    }
}