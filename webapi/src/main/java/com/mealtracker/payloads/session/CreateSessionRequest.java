package com.mealtracker.payloads.session;

import lombok.Data;

@Data
public class CreateSessionRequest {

    private String email;

    private String password;
}
