package com.app.payment.web.dto;

import com.app.payment.model.Currency;
import com.app.payment.model.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@Builder
public class MakePaymentDTO {

    private final String customerId;
    private final BigDecimal amount;
    private final Currency currency;
    private final String cardSource;
    private final String description;

    public Payment toPayment() {
        return Payment.builder()
                .customerId(customerId)
                .amount(amount)
                .currency(currency)
                .cardSource(cardSource)
                .description(description)
                .build();
    }

}
