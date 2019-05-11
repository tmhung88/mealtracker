package com.mealtracker.payloads.user;

import com.mealtracker.domains.User;
import com.mealtracker.payloads.MetaSuccessEnvelop;
import com.mealtracker.payloads.PaginationMeta;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LookupUserInfoResponse {
    private final long id;
    private final String email;
    private final String fullName;

    public static MetaSuccessEnvelop<List<LookupUserInfoResponse>, PaginationMeta> envelop(Page<User> userPage) {
        var users = userPage.getContent().stream().map(LookupUserInfoResponse::new).collect(Collectors.toList());
        return new MetaSuccessEnvelop<>(users, PaginationMeta.of(userPage));
    }

    public LookupUserInfoResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
    }
}
