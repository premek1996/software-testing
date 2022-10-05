package com.app.payment.service.cardpaymentcharger.stripe;

import com.app.payment.model.Currency;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharge;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        return CardPaymentCharge.builder()
                .isCardDebited(true)
                .build();
    }

}
