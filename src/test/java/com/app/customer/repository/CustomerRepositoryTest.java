package com.app.customer.repository;

import com.app.customer.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("it should find customer by phone number")
    void test1() {
        var phoneNumber = "999999999";
        var customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("name")
                .phoneNumber(phoneNumber)
                .build();
        customerRepository.save(customer);
        var foundCustomerOpt = customerRepository.findByPhoneNumber(phoneNumber);
        assertThat(foundCustomerOpt)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(customer);
    }

    @Test
    @DisplayName("it should not find customer by phone number")
    void test2() {
        var customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name("name")
                .phoneNumber("999999999")
                .build();
        customerRepository.save(customer);
        var foundCustomerOpt = customerRepository.findByPhoneNumber("1111111");
        assertThat(foundCustomerOpt)
                .isEmpty();
    }

    @Test
    @DisplayName("it should not save customer when name is null")
    void test3() {
        var id = UUID.randomUUID().toString();
        var customer = Customer.builder()
                .id(id)
                .phoneNumber("999999999")
                .build();
        assertThatThrownBy(() -> customerRepository.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(customerRepository.findById(id))
                .isEmpty();
    }

    @Test
    @DisplayName("it should not save customer when phone number is null")
    void test4() {
        var id = UUID.randomUUID().toString();
        var customer = Customer.builder()
                .id(id)
                .name("name")
                .build();
        assertThatThrownBy(() -> customerRepository.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(customerRepository.findById(id)).
                isEmpty();
    }

}