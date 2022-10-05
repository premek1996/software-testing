package com.app.utils;

public interface ExceptionMessage {

    String OTHER_CUSTOMER_HAS_THE_SAME_PHONE_NUMBER = "Other customer has the same phone number";
    String CUSTOMER_DOES_NOT_EXIST_WITH_GIVEN_ID = "Customer does not exist with given id";
    String CARD_IS_NOT_DEBITED = "Card is not debited";
    String CURRENCY_IS_NOT_ACCEPTED = "Currency is not accepted";
    String CANNOT_MAKE_STRIPE_CHARGE = "Cannot make stripe charge";
    String PHONE_NUMBER_IS_INVALID = "Phone number is invalid";

}
