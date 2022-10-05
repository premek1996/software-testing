package com.app.payment.service.messagesender;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MessageStatus {
    private final boolean isSent;
}
