package com.app.payment.service.messagesender;

public interface MessageSender {
    MessageStatus sendSMS(String phoneNumber);
}
