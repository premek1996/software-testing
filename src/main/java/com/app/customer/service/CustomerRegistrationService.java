package com.app.customer.service;

import com.app.customer.model.Customer;
import com.app.customer.model.validator.PhoneNumberValidator;
import com.app.customer.repository.CustomerRepository;
import com.app.customer.web.dto.CustomerRegistrationDTO;
import com.app.exception.BadRequestException;
import com.app.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private final PhoneNumberValidator phoneNumberValidator;

    public Customer registerNewCustomer(CustomerRegistrationDTO dto) {
        if (!phoneNumberValidator.validate(dto.getPhoneNumber())) {
            throw new BadRequestException(ExceptionMessage.PHONE_NUMBER_IS_INVALID);
        }
        var customerOpt = customerRepository
                .findByPhoneNumber(dto.getPhoneNumber());
        if (customerOpt.isEmpty()) {
            return customerRepository.save(dto.toCustomer());
        }
        var customer = customerOpt.get();
        if (customer.hasName(dto.getName())) {
            return customer;
        }
        throw new BadRequestException
                (ExceptionMessage.OTHER_CUSTOMER_HAS_THE_SAME_PHONE_NUMBER);
    }

}
