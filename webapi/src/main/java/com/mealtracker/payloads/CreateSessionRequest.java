package com.mealtracker.payloads;

import lombok.Data;

@Data
public class CreateSessionRequest {

    private String email;

    private String password;
}
