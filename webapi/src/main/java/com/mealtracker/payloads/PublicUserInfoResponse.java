package com.mealtracker.payloads;

import com.mealtracker.domains.User;
import lombok.Value;

@Value
public class PublicUserInfoResponse {
    private final String fullName;
    private final String email;

    public static SuccessEnvelop<PublicUserInfoResponse> of(User user) {
        return new SuccessEnvelop<>(new PublicUserInfoResponse(user.getFullName(), user.getEmail()));
    }
}
