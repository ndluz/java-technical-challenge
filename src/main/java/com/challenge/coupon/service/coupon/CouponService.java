package com.challenge.coupon.service.coupon;

import com.challenge.coupon.controller.api.CouponCreateRequest;
import com.challenge.coupon.domain.entity.coupon.Coupon;
import com.challenge.coupon.domain.entity.coupon.CouponStatus;
import com.challenge.coupon.domain.entity.exception.*;
import com.challenge.coupon.repository.CouponRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(CouponService.class);

    private String replaceCode(String code) {
        return code.replaceAll("[^a-zA-Z0-9]", "");
    }

    private void validateMinimumDiscountValue(BigDecimal discountValue) {
        if (discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw new InvalidDiscountValueException("Discount amount shown is less than the minimum allowed");
        }
    }

    private void validateExpirationDate(LocalDateTime expirationDate) {
        LocalDateTime now = LocalDateTime.now();

        if (expirationDate.isBefore(now)) {
            throw new InvalidExpirationDateException("The expiration date must be a future date");
        }
    }

    private void validate(CouponCreateRequest request) {
        validateMinimumDiscountValue(request.discountValue());
        validateExpirationDate(request.expirationDate());
    }

    @Transactional
    public Coupon create(CouponCreateRequest request) {

        validate(request);

        String replacedCode = replaceCode(request.code());

        if (replacedCode.length() != 6) {
            throw new InvalidCodeException("Coupon code must contain exactly 6 alphanumeric characters");
        }

        if (couponRepository.existsByCodeAndStatus(replacedCode, CouponStatus.ACTIVE)) {
            throw new DuplicateCodeException("Coupon code already exists");
        }

        Coupon coupon = new Coupon();

        coupon.setCode(replacedCode);
        coupon.setDescription(request.description());
        coupon.setExpirationDate(request.expirationDate());
        coupon.setPublished(request.published());
        coupon.setDiscountValue(request.discountValue());
        coupon.setStatus(CouponStatus.ACTIVE);
        coupon.setRedeemed(false);

        Coupon registered = couponRepository.save(coupon);

        log.info(
                "Registered coupon with code={}, description={} and expirationDate={} successfully",
                coupon.getCode(), coupon.getDescription(), coupon.getExpirationDate()
        );

        return registered;
    }

    private UUID parseIdStringToUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUuidException("Invalid coupon ID format");
        }
    }

    @Transactional
    public void delete(String id) {

       Coupon coupon = couponRepository.findById(parseIdStringToUuid(id)).orElseThrow(() ->
               new DomainNotFoundException("Coupon not found")
       );

       if (coupon.getStatus().equals(CouponStatus.DELETED)) {
           throw new DomainException("You cannot delete a coupon that has already been deleted");
       }

       coupon.setStatus(CouponStatus.DELETED);

       couponRepository.save(coupon);
       log.info("Coupon with id={} was successfully deleted", coupon.getId());
    }

    public Coupon findById(String id) {

        return couponRepository.findById(parseIdStringToUuid(id)).orElseThrow(() ->
                new DomainNotFoundException("Coupon not found")
        );
    }
}