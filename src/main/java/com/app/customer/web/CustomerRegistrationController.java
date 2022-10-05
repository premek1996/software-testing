package com.app.customer.web;

import com.app.customer.service.CustomerRegistrationService;
import com.app.customer.web.dto.CustomerRegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/customer-registration")
@RequiredArgsConstructor
public class CustomerRegistrationController {

    private final CustomerRegistrationService customerRegistrationService;

    @PutMapping
    public void registerNewCustomer(@RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        customerRegistrationService.registerNewCustomer(customerRegistrationDTO);
    }

}
