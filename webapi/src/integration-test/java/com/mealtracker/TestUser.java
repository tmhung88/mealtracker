package com.mealtracker;

import com.mealtracker.domains.Privilege;
import com.mealtracker.domains.Role;
import lombok.Builder;
import lombok.Value;

import java.util.List;

import static com.mealtracker.domains.Privilege.MEAL_MANAGEMENT;
import static com.mealtracker.domains.Privilege.MY_MEALS;
import static com.mealtracker.domains.Privilege.USER_MANAGEMENT;
import static java.util.Arrays.asList;

@Value
@Builder
public class TestUser {

    private final long id;
    private final String email;
    private final String fullName;
    private final boolean enabled;
    private final Role role;
    private final List<Privilege> privileges;
    private final String token;

    public static final TestUser ADMIN = TestUser.builder()
            .id(1L)
            .email("admin@gmail.com")
            .fullName("Admin")
            .enabled(true)
            .role(Role.ADMIN).privileges(asList(MY_MEALS, MEAL_MANAGEMENT, USER_MANAGEMENT))
            .token("Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcml2aWxlZ2VzIjpbIk1ZX01FQUxTIiwiVVNFUl9NQU5BR0VNRU5UIiwiTUVBTF9NQU5BR0VNRU5UIl0sInJvbGUiOiJBRE1JTiIsImZ1bGxOYW1lIjoiQWRtaW4iLCJpZCI6MSwiZW1haWwiOiJhZG1pbkBnbWFpbC5jb20ifQ.zm20SbhXds-_aRmEhS3lZpaA55eC8iAqDM_npcCu91TFF3L1f3QbscsP7-fnspTrFCpUgAbp_oXeSA0xPqo_gg")
            .build();


    public static final TestUser USER_MANAGER = TestUser.builder()
            .id(2L)
            .email("user_manager@gmail.com")
            .fullName("User Manager")
            .enabled(true)
            .role(Role.USER_MANAGER).privileges(asList(MY_MEALS, USER_MANAGEMENT))
            .token("Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcml2aWxlZ2VzIjpbIk1ZX01FQUxTIiwiVVNFUl9NQU5BR0VNRU5UIl0sInJvbGUiOiJVU0VSX01BTkFHRVIiLCJmdWxsTmFtZSI6IlVzZXIgTWFuYWdlciIsImlkIjoyLCJlbWFpbCI6InVzZXJfbWFuYWdlckBnbWFpbC5jb20ifQ.yhkjyFvRsGwLRE2lAKldelpZbs1JYXKGnCnKB_iNcRV7rTk1lODTqSv96O2-gm4ta8DbHyQFSlIhaYLi2QtBGA")
            .build();


    public static final TestUser USER = TestUser.builder()
            .id(3L)
            .email("regular_user@gmail.com")
            .fullName("Regular User")
            .enabled(true)
            .role(Role.REGULAR_USER).privileges(asList(MY_MEALS))
            .token("Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcml2aWxlZ2VzIjpbIk1ZX01FQUxTIl0sInJvbGUiOiJSRUdVTEFSX1VTRVIiLCJmdWxsTmFtZSI6IlJlZ3VsYXIgVXNlciIsImlkIjozLCJlbWFpbCI6InJlZ3VsYXJfdXNlckBnbWFpbC5jb20ifQ.p_VOz4XdeG8oxIjoZAN2vm-xxXt_FYH9sphVhmZEDdl3GPtHefHk4pPtxZ9yxrnhSQvrfJKDUMLoNgs-XkNW1w")
            .build();


}
