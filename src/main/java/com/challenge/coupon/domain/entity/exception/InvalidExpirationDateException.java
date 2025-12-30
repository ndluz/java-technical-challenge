package com.challenge.coupon.domain.entity.exception;

public class InvalidExpirationDateException extends DomainException {

    public InvalidExpirationDateException(String message) {
        super(message);
    }
}
