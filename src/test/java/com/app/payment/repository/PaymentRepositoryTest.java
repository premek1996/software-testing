package com.app.payment.repository;

import com.app.customer.model.Customer;
import com.app.customer.repository.CustomerRepository;
import com.app.payment.model.Currency;
import com.app.payment.model.Payment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("it should insert payment")
    void test1() {
        var paymentId = 1L;
        var payment = Payment.builder()
                .paymentId(paymentId)
                .customerId(UUID.randomUUID().toString())
                .amount(BigDecimal.TEN)
                .currency(Currency.USD)
                .cardSource("card123")
                .description("Donation")
                .build();

        paymentRepository.save(payment);
        assertThat(paymentRepository.findById(paymentId))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(payment);
    }

    @Test
    @DisplayName("it should find all payments by customerId")
    void test2() {

        var customerId = UUID.randomUUID().toString();
        var customer = Customer.builder()
                .id(customerId)
                .name("name")
                .phoneNumber("99999999")
                .build();
        customerRepository.save(customer);

        var payment = Payment.builder()
                .customerId(customerId)
                .amount(BigDecimal.TEN)
                .currency(Currency.USD)
                .cardSource("x0x0x0x0")
                .description("Donation")
                .build();

        paymentRepository.save(payment);

        Assertions.assertThat(paymentRepository.getAllByCustomerId(customerId))
                .hasSize(1);
    }


}