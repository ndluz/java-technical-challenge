package com.challenge.coupon.service.coupon;

import com.challenge.coupon.controller.api.CouponCreateRequest;
import com.challenge.coupon.domain.entity.coupon.Coupon;
import com.challenge.coupon.domain.entity.coupon.CouponStatus;
import com.challenge.coupon.domain.entity.exception.DomainException;
import com.challenge.coupon.domain.entity.exception.DomainNotFoundException;
import com.challenge.coupon.domain.entity.exception.InvalidCodeException;
import com.challenge.coupon.domain.entity.exception.InvalidExpirationDateException;
import com.challenge.coupon.repository.CouponRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    private static final LocalDateTime VALID_EXPIRATION = LocalDateTime.now().plusDays(30);
    private static final LocalDateTime INVALID_EXPIRATION = LocalDateTime.now().minusDays(1);

    @Test
    @DisplayName("Should create a coupon with success when all data are valid")
    void shouldCreateASuccessfullyCoupon() {

        CouponCreateRequest request = new CouponCreateRequest(
                "ABC123",
                "Test discount",
                BigDecimal.valueOf(10.00),
                VALID_EXPIRATION,
                true
        );

        Coupon savedCoupon = new Coupon();
        savedCoupon.setId(UUID.randomUUID());
        savedCoupon.setCode("ABC123");
        savedCoupon.setDescription("It is a test discount");
        savedCoupon.setDiscountValue(BigDecimal.valueOf(5.00));
        savedCoupon.setExpirationDate(VALID_EXPIRATION);
        savedCoupon.setStatus(CouponStatus.ACTIVE);
        savedCoupon.setPublished(true);
        savedCoupon.setRedeemed(false);

        when(couponRepository.save(any(Coupon.class))).thenReturn(savedCoupon);

        Coupon result = couponService.create(request);

        assertNotNull(result);
        assertEquals("ABC123", result.getCode());
        assertEquals(CouponStatus.ACTIVE, result.getStatus());
        assertFalse(result.getRedeemed());
        verify(couponRepository).save(any(Coupon.class));

    }

    @Test
    @DisplayName("Should throw InvalidCodeException when coupon code does not have 6 characters ")
    void shouldThrowAExceptionWhenCouponCodeDoesNotHaveSixCharacters() {
        CouponCreateRequest request = new CouponCreateRequest(
                "ABC12",
                "Description test",
                BigDecimal.valueOf(5),
                VALID_EXPIRATION,
                true
        );

        InvalidCodeException exception = assertThrows(InvalidCodeException.class, () ->
                couponService.create(request));

        assertEquals("Coupon code must contain exactly 6 alphanumeric characters", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidExpirationDate when expiration date is a invalid date")
    void shouldThrowExceptionWhenExpirationDateIsInvalid() {
        CouponCreateRequest request = new CouponCreateRequest(
                "ABC123",
                "Desc",
                BigDecimal.TEN,
                INVALID_EXPIRATION,
                true
        );

        InvalidExpirationDateException exception = assertThrows(InvalidExpirationDateException.class, () ->
                couponService.create(request));

        assertEquals("The expiration date must be a future date", exception.getMessage());
    }

    @Test
    @DisplayName("Should soft delete a coupon successfully")
    void shouldSoftDeleteACouponSuccessfully() {
        UUID couponId = UUID.randomUUID();
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        coupon.setStatus(CouponStatus.ACTIVE);

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        couponService.delete(couponId.toString());

        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        verify(couponRepository).save(coupon);
    }

    @Test
    @DisplayName("Should throw DomainException when trying to delete an already deleted coupon")
    void shouldThrowExceptionWhenCouponAlreadyDeleted() {
        UUID couponId = UUID.randomUUID();
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        coupon.setStatus(CouponStatus.DELETED);

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        DomainException exception = assertThrows(DomainException.class, () ->
                couponService.delete(couponId.toString()));

        assertEquals("You cannot delete a coupon that has already been deleted", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw DomainNotFoundException when coupon does not exists")
    void shouldThrowExceptionWhenCouponDoesNotExists() {
        UUID randomId = UUID.randomUUID();

        when(couponRepository.findById(randomId)).thenReturn(Optional.empty());

        DomainNotFoundException exception = assertThrows(DomainNotFoundException.class, () ->
                couponService.delete(randomId.toString()));

        assertEquals("Coupon not found", exception.getMessage());
    }
}