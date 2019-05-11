package com.mealtracker.services.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class SessionInput {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;
}
