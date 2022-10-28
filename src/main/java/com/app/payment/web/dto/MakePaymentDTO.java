package com.app.payment.web.dto;

import com.app.payment.model.Currency;
import com.app.payment.model.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@Builder
public class MakePaymentDTO {

    @NotBlank
    private final String customerId;

    @NotNull
    private final BigDecimal amount;

    @NotNull
    private final Currency currency;

    @NotBlank
    private final String cardSource;

    @NotBlank
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
