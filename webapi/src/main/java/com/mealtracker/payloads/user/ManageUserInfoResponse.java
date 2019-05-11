package com.mealtracker.payloads.user;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.payloads.MetaSuccessEnvelop;
import com.mealtracker.payloads.PaginationMeta;
import com.mealtracker.payloads.SuccessEnvelop;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ManageUserInfoResponse {
    private final long id;
    private final String email;
    private final String fullName;
    private final Role role;
    private final long dailyCalorieLimit;

    public static MetaSuccessEnvelop<List<ManageUserInfoResponse>, PaginationMeta> envelop(Page<User> userPage) {
        var users = userPage.getContent().stream().map(ManageUserInfoResponse::new).collect(Collectors.toList());
        return new MetaSuccessEnvelop<>(users, PaginationMeta.of(userPage));
    }

    public static SuccessEnvelop<ManageUserInfoResponse> envelop(User user) {
        return new SuccessEnvelop<>(new ManageUserInfoResponse(user));
    }

    private ManageUserInfoResponse(User user) {
        id = user.getId();
        email = user.getEmail();
        fullName = user.getFullName();
        role = user.getRole();
        dailyCalorieLimit = user.getUserSettings().getDailyCalorieLimit();
    }
}
