package com.app.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneNumberValidatorTest {

    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setUp() {
        phoneNumberValidator = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"+447000000000", "+447000800010"})
    @DisplayName("it should validate valid phone number")
    void test1(String phoneNumber) {
        boolean isValid = phoneNumberValidator.validate(phoneNumber);
        assertThat(isValid).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"+447000000000000", "447000000000000"})
    @DisplayName("it should validate invalid phone number")
    void test2(String phoneNumber) {
        boolean isValid = phoneNumberValidator.validate(phoneNumber);
        assertThat(isValid).isFalse();
    }

}
