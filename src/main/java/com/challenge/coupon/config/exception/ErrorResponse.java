package com.challenge.coupon.config.exception;

import java.util.List;

public record ErrorResponse(

        int status,
        List<String> errors
) {}