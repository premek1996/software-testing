package com.app.payment;

import com.app.customer.web.dto.CustomerRegistrationDTO;
import com.app.payment.model.Currency;
import com.app.payment.model.Payment;
import com.app.payment.web.dto.MakePaymentDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("it should create payment successfully")
    @SneakyThrows
    void test1() {
        var customerId = UUID.randomUUID().toString();

        var customerRegistrationDTO = CustomerRegistrationDTO.builder()
                .customerId(customerId)
                .name("James")
                .phoneNumber("+447000000000")
                .build();

        var customerRegistrationResultActions = mockMvc.perform(post("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRegistrationDTO)));

        var makePaymentDTO = MakePaymentDTO.builder()
                .customerId(customerId)
                .amount(BigDecimal.TEN)
                .currency(Currency.USD)
                .cardSource("x0x0x0x0")
                .description("Donation")
                .build();

        var makePaymentResultAction = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(makePaymentDTO)));

        var paymentsByCustomerIdResultAction = mockMvc.perform(get("/api/v1/payment/%s".formatted(customerId)));

        customerRegistrationResultActions.andExpect(status().isOk());
        makePaymentResultAction.andExpect(status().isOk());
        paymentsByCustomerIdResultAction.andExpect(status().isOk());

        var payments = getPaymentsFromResultActions(paymentsByCustomerIdResultAction);
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).isMessageSent())
                .isTrue();
    }

    @SneakyThrows
    private List<Payment> getPaymentsFromResultActions(ResultActions resultActions) {
        var responseAsString = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        return Arrays.asList(objectMapper.readValue(responseAsString, Payment[].class));
    }

}
