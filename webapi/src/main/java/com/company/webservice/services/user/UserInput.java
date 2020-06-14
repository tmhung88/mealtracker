package com.company.webservice.services.user;

import com.company.webservice.domains.User;
import com.company.webservice.validation.OnAdd;
import com.company.webservice.validation.OnUpdate;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserInput {

    @NotNull(groups = {OnAdd.class, OnUpdate.class})
    @Email(groups = {OnAdd.class, OnUpdate.class})
    @Size(min = 5, max = 200, groups = {OnAdd.class, OnUpdate.class})
    private String email;

    @NotNull(groups = {OnAdd.class, OnUpdate.class})
    @Size(min = 5, max = 200, groups = {OnAdd.class, OnUpdate.class})
    private String fullName;

    public User toUser() {
        var user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        return user;
    }
}
