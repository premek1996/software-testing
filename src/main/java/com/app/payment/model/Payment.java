package com.app.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Payment {

    @Id
    @GeneratedValue
    private Long paymentId;
    private String customerId;
    private BigDecimal amount;
    private Currency currency;
    private String cardSource;
    private String description;
    private boolean messageSent;

    public Payment withMessageSent(boolean messageSent) {
        return Payment.builder()
                .paymentId(paymentId)
                .customerId(customerId)
                .amount(amount)
                .currency(currency)
                .cardSource(cardSource)
                .description(description)
                .messageSent(messageSent)
                .build();
    }

}
