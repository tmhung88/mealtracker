package com.mealtracker.payloads;

public class UserListResponse {
    public static SuccessEnvelop<UserListResponse> of() {
        return new SuccessEnvelop<>(new UserListResponse());
    }
}
