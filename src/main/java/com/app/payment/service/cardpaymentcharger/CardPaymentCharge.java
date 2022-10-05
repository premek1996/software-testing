package com.app.payment.service.cardpaymentcharger;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardPaymentCharge {
    private final boolean isCardDebited;
}
