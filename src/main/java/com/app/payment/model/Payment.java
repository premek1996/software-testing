package com.app.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private String cardSource;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
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
