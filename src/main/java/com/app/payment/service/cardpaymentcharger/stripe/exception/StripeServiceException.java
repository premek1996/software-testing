package com.app.payment.service.cardpaymentcharger.stripe.exception;

public class StripeServiceException extends RuntimeException {
    public StripeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
