package com.app.payment.service;

import com.app.customer.model.Customer;
import com.app.customer.repository.CustomerRepository;
import com.app.payment.model.Currency;
import com.app.payment.model.Payment;
import com.app.payment.repository.PaymentRepository;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharge;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharger;
import com.app.payment.service.exception.PaymentServiceException;
import com.app.payment.service.messagesender.MessageSender;
import com.app.payment.service.messagesender.MessageStatus;
import com.app.payment.web.dto.MakePaymentDTO;
import com.app.utils.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;
    private final MessageSender messageSender;
    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD);

    public void chargeCard(MakePaymentDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new PaymentServiceException(ExceptionMessage.CUSTOMER_DOES_NOT_EXIST_WITH_GIVEN_ID));

        if (!isCurrencyAccepted(dto.getCurrency())) {
            throw new PaymentServiceException(ExceptionMessage.CURRENCY_IS_NOT_ACCEPTED);
        }

        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                dto.getCardSource(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription());

        if (!cardPaymentCharge.isCardDebited()) {
            throw new PaymentServiceException(ExceptionMessage.CARD_IS_NOT_DEBITED);
        }

        MessageStatus messageStatus = messageSender.sendSMS(customer.getPhoneNumber());

        Payment payment = dto.toPayment().
                withMessageSent(messageStatus.isSent());

        paymentRepository.save(payment);
    }

    private static boolean isCurrencyAccepted(Currency currency) {
        return ACCEPTED_CURRENCIES.contains(currency);
    }

    public List<Payment> getAllByCustomerId(String customerId) {
        return paymentRepository.getAllByCustomerId(customerId);
    }

}
