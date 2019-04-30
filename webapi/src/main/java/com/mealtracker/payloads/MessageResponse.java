package com.mealtracker.payloads;

import lombok.Value;

@Value
public class MessageResponse {
    private final String message;

    public static SuccessEnvelop<MessageResponse> of(String message) {
        return new SuccessEnvelop<>(new MessageResponse(message));
    }
}
