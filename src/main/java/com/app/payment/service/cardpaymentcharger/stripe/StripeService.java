package com.app.payment.service.cardpaymentcharger.stripe;

import com.app.exception.ExceptionMessage;
import com.app.payment.model.Currency;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharge;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharger;
import com.app.payment.service.cardpaymentcharger.stripe.exception.StripeServiceException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "true"
)
@RequiredArgsConstructor
public class StripeService implements CardPaymentCharger {

    private final RequestOptions requestOptions;

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("amount", amount);
        requestMap.put("currency", currency);
        requestMap.put("source", cardSource);
        requestMap.put("description", description);
        try {
            Charge charge = Charge.create(requestMap, requestOptions);
            return CardPaymentCharge.builder()
                    .isCardDebited(charge.getPaid())
                    .build();
        } catch (Exception e) {
            throw new StripeServiceException(ExceptionMessage.CANNOT_MAKE_STRIPE_CHARGE, e);
        }
    }

}
