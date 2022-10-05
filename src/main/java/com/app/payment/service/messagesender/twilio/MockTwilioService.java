package com.app.payment.service.messagesender.twilio;

import com.app.payment.service.messagesender.MessageSender;
import com.app.payment.service.messagesender.MessageStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        value = "twilio.enabled",
        havingValue = "false"
)
public class MockTwilioService implements MessageSender {

    @Override
    public MessageStatus sendSMS(String phoneNumber) {
        return MessageStatus.builder()
                .isSent(true)
                .build();
    }

}
