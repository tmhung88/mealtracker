package com.mealtracker.payloads.meal;

import com.mealtracker.domains.User;
import lombok.Data;

@Data
public class BriefUserResponse {
    private final long id;
    private final String email;
    private final String fullName;

    public BriefUserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }
}
