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

    public static final TestUser NO_MY_MEAL = TestUser.builder()
            .id(4L)
            .email("no_my_meal@gmail.com")
            .fullName("No My Meal")
            .enabled(true)
            .role(Role.REGULAR_USER).privileges(TestPrivilege.exclude(MY_MEALS))
            .token("Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcml2aWxlZ2VzIjpbIlVTRVJfTUFOQUdFTUVOVCIsIk1FQUxfTUFOQUdFTUVOVCJdLCJyb2xlIjoiTk9fTVlfTUVBTCIsImZ1bGxOYW1lIjoiTm8gTXkgTWVhbCIsImlkIjo0LCJlbWFpbCI6Im5vX215X21lYWxAZ21haWwuY29tIn0.WHOysasy6N5leMCTfP7jfmp015Yr3-rWbIGB0HM7t6HDfNJJe4nZZvRFyFLfnIg5qSGTf7HsTDsGFgmf1HD8QA")
            .build();

    public static final TestUser NO_USER_MANAGEMENT = TestUser.builder()
            .id(5L)
            .email("no_user_managent@gmail.com")
            .fullName("No User Management")
            .enabled(true)
            .role(Role.REGULAR_USER).privileges(TestPrivilege.exclude(USER_MANAGEMENT))
            .token("Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcml2aWxlZ2VzIjpbIk1ZX01FQUxTIiwiTUVBTF9NQU5BR0VNRU5UIl0sInJvbGUiOiJOT19VU0VSX01BTkFHRU1FTlQiLCJmdWxsTmFtZSI6Ik5vIFVzZXIgTWFuYWdlbWVudCIsImlkIjo1LCJlbWFpbCI6Im5vX3VzZXJfbWFuYWdlbnRAZ21haWwuY29tIn0.9n-dta54Vz_92c5RQP86AHzds8ZnySdQjvtI1LCFFy1-Mwy6tHAbA4pUxplibj-vCtZ61p4HDTkHcWfyVZDGRQ")
            .build();

    public static final TestUser NO_MEAL_MANAGEMENT = TestUser.builder()
            .id(6L)
            .email("no_meal_managent@gmail.com")
            .fullName("No Meal Management")
            .enabled(true)
            .role(Role.REGULAR_USER).privileges(TestPrivilege.exclude(MEAL_MANAGEMENT))
            .token("Bearer eyJhbGciOiJIUzUxMiJ9.eyJwcml2aWxlZ2VzIjpbIk1ZX01FQUxTIiwiVVNFUl9NQU5BR0VNRU5UIl0sInJvbGUiOiJOT19NRUFMX01BTkFHRU1FTlQiLCJmdWxsTmFtZSI6Ik5vIE1lYWwgTWFuYWdlbWVudCIsImlkIjo2LCJlbWFpbCI6Im5vX21lYWxfbWFuYWdlbnRAZ21haWwuY29tIn0.8sYrfGXS0loUD2P75_py796HzOz4W-RbDVzI2rr_zCujn7IbzVtlAnfPqcT4GBnNMZFhkoEWVIIsQ7EgZd-7mw")
            .build();
}
