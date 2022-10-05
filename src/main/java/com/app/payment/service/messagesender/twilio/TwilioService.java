package com.app.payment.service.messagesender.twilio;

import com.app.payment.service.messagesender.MessageSender;
import com.app.payment.service.messagesender.MessageStatus;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        value = "twilio.enabled",
        havingValue = "true"
)
public class TwilioService implements MessageSender {

    @Value("${twilio_account_sid}")
    private String twilioAccountSid;

    @Value("${twilio_auth_token}")
    private String twilioAuthToken;


    @Override
    public MessageStatus sendSMS(String phoneNumber) {
        boolean isSent = false;
        try {
            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message message = Message.creator(
                            new PhoneNumber(phoneNumber),
                            new PhoneNumber("+15017122661"),
                            "Hi there")
                    .create();
            isSent = message.getStatus()
                    .equals(Message.Status.SENT);
        } catch (Exception ignored) {

        }
        return MessageStatus.builder()
                .isSent(isSent)
                .build();
    }

}
