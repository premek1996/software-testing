package com.app.utils;

import org.springframework.stereotype.Service;

@Service
public class PhoneNumberValidator {

    public boolean validate(String phoneNumber) {
        return phoneNumber.startsWith("+44")
                && phoneNumber.length() == 13;
    }

}
