package com.app.payment.service;

import com.app.customer.model.Customer;
import com.app.customer.repository.CustomerRepository;
import com.app.exception.BadRequestException;
import com.app.exception.ExceptionMessage;
import com.app.payment.model.Currency;
import com.app.payment.model.Payment;
import com.app.payment.repository.PaymentRepository;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharge;
import com.app.payment.service.cardpaymentcharger.CardPaymentCharger;
import com.app.payment.service.messagesender.MessageSender;
import com.app.payment.service.messagesender.MessageStatus;
import com.app.payment.web.dto.MakePaymentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CardPaymentCharger cardPaymentCharger;

    @Mock
    private MessageSender messageSender;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("it should throw exception when customer does not exist")
    void test1() {
        var dto = MakePaymentDTO.builder()
                .customerId(UUID.randomUUID().toString())
                .amount(BigDecimal.TEN)
                .currency(Currency.USD)
                .cardSource("card123")
                .description("Donation")
                .build();

        when(customerRepository.findById(dto.getCustomerId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.chargeCard(dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.CUSTOMER_DOES_NOT_EXIST_WITH_GIVEN_ID);

        verifyNoInteractions(cardPaymentCharger);
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("it should throw exception when currency is not accepted")
    void test2() {
        var dto = MakePaymentDTO.builder()
                .customerId(UUID.randomUUID().toString())
                .amount(BigDecimal.TEN)
                .currency(Currency.GBP)
                .cardSource("card123")
                .description("Donation")
                .build();

        when(customerRepository.findById(dto.getCustomerId()))
                .thenReturn(Optional.of(mock(Customer.class)));

        assertThatThrownBy(() -> paymentService.chargeCard(dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.CURRENCY_IS_NOT_ACCEPTED);

        verifyNoInteractions(cardPaymentCharger);
        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("it should throw exception when card is not debited")
    void test3() {
        var dto = MakePaymentDTO.builder()
                .customerId(UUID.randomUUID().toString())
                .amount(BigDecimal.TEN)
                .currency(Currency.USD)
                .cardSource("card123")
                .description("Donation")
                .build();

        when(customerRepository.findById(dto.getCustomerId()))
                .thenReturn(Optional.of(mock(Customer.class)));

        var cardPaymentCharge = CardPaymentCharge.builder()
                .isCardDebited(false)
                .build();

        when(cardPaymentCharger.chargeCard(dto.getCardSource(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription()))
                .thenReturn(cardPaymentCharge);

        assertThatThrownBy(() -> paymentService.chargeCard(dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.CARD_IS_NOT_DEBITED);

        verifyNoInteractions(paymentRepository);
    }

    @Test
    @DisplayName("it should save payment and send sms")
    void test4() {
        var dto = MakePaymentDTO.builder()
                .customerId(UUID.randomUUID().toString())
                .amount(BigDecimal.TEN)
                .currency(Currency.USD)
                .cardSource("card123")
                .description("Donation")
                .build();

        var customer = Customer.builder()
                .phoneNumber("999999999")
                .build();

        when(customerRepository.findById(dto.getCustomerId()))
                .thenReturn(Optional.of(customer));

        var cardPaymentCharge = CardPaymentCharge.builder()
                .isCardDebited(true)
                .build();

        when(cardPaymentCharger.chargeCard(dto.getCardSource(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription()))
                .thenReturn(cardPaymentCharge);

        when(messageSender.sendSMS(customer.getPhoneNumber()))
                .thenReturn(MessageStatus.builder().isSent(true).build());

        assertThatNoException().isThrownBy(() -> paymentService.chargeCard(dto));

        verify(paymentRepository, times(1))
                .save(any(Payment.class));
    }

    @Test
    @DisplayName("it should find all payments by customerId")
    void test5() {
        var customerId = UUID.randomUUID().toString();

        var payment = Payment.builder()
                .customerId(customerId)
                .amount(BigDecimal.TEN)
                .currency(Currency.USD)
                .cardSource("x0x0x0x0")
                .description("Donation")
                .build();

        when(paymentRepository.getAllByCustomerId(customerId))
                .thenReturn(List.of(payment));

        assertThat(paymentService.getAllByCustomerId(customerId))
                .containsExactly(payment);
    }

}