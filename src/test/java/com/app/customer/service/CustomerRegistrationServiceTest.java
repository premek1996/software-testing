package com.app.customer.service;

import com.app.customer.model.Customer;
import com.app.customer.repository.CustomerRepository;
import com.app.customer.service.exception.CustomerRegistrationServiceException;
import com.app.customer.web.dto.CustomerRegistrationDTO;
import com.app.utils.ExceptionMessage;
import com.app.utils.PhoneNumberValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    @InjectMocks
    private CustomerRegistrationService customerRegistrationService;

    @Test
    @DisplayName("is should save new customer")
    void test1() {
        var dto = CustomerRegistrationDTO.builder()
                .name("name")
                .phoneNumber("999999999")
                .build();

        when(phoneNumberValidator.validate(dto.getPhoneNumber()))
                .thenReturn(true);

        when(customerRepository.findByPhoneNumber(dto.getPhoneNumber()))
                .thenReturn(Optional.empty());

        customerRegistrationService.registerNewCustomer(dto);

        verify(customerRepository, times(1))
                .findByPhoneNumber(dto.getPhoneNumber());

        verify(customerRepository, times(1))
                .save(any(Customer.class));

        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    @DisplayName("it should not save customer when customer exists")
    void test2() {
        var dto = CustomerRegistrationDTO.builder()
                .name("name")
                .phoneNumber("999999999")
                .build();

        when(phoneNumberValidator.validate(dto.getPhoneNumber()))
                .thenReturn(true);

        when(customerRepository.findByPhoneNumber(dto.getPhoneNumber()))
                .thenReturn(Optional.of(dto.toCustomer()));

        assertThatNoException()
                .isThrownBy(() -> customerRegistrationService.registerNewCustomer(dto));

        verify(customerRepository, times(1))
                .findByPhoneNumber(dto.getPhoneNumber());

        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    @DisplayName("it should throw exception when phone number is taken")
    void test3() {
        var dto = CustomerRegistrationDTO.builder()
                .name("name")
                .phoneNumber("999999999")
                .build();

        when(phoneNumberValidator.validate(dto.getPhoneNumber()))
                .thenReturn(true);

        when(customerRepository.findByPhoneNumber(dto.getPhoneNumber()))
                .thenReturn(Optional.of(Customer.builder()
                        .name("other customer")
                        .build()));

        assertThatThrownBy(() -> customerRegistrationService.registerNewCustomer(dto))
                .isInstanceOf(CustomerRegistrationServiceException.class)
                .hasMessage(ExceptionMessage.OTHER_CUSTOMER_HAS_THE_SAME_PHONE_NUMBER);

        verify(customerRepository, times(1))
                .findByPhoneNumber(dto.getPhoneNumber());

        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    @DisplayName("it should throw exception when phone number is invalid")
    void test4() {
        var dto = CustomerRegistrationDTO.builder()
                .name("name")
                .phoneNumber("999999999")
                .build();

        when(phoneNumberValidator.validate(dto.getPhoneNumber()))
                .thenReturn(false);

        assertThatThrownBy(() -> customerRegistrationService.registerNewCustomer(dto))
                .isInstanceOf(CustomerRegistrationServiceException.class)
                .hasMessage(ExceptionMessage.PHONE_NUMBER_IS_INVALID);

        verifyNoInteractions(customerRepository);
    }

}