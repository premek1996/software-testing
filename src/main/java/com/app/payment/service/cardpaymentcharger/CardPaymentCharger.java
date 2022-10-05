package com.app.payment.service.cardpaymentcharger;

import com.app.payment.model.Currency;

import java.math.BigDecimal;

public interface CardPaymentCharger {

    CardPaymentCharge chargeCard(String cardSource,
                                 BigDecimal amount,
                                 Currency currency,
                                 String description);
}
