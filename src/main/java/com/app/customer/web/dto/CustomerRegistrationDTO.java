package com.app.customer.web.dto;

import com.app.customer.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CustomerRegistrationDTO {

    private String customerId;

    @NotBlank
    private final String name;

    @NotBlank
    private final String phoneNumber;

    public Customer toCustomer() {
        if (customerId == null) {
            customerId = UUID.randomUUID().toString();
        }
        return Customer.builder()
                .id(customerId)
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
    }

}
