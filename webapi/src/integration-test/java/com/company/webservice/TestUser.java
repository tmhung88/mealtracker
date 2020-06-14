package com.company.webservice;

import lombok.Value;

@Value
public class TestUser {

    private final long id;
    private final String email;
    private final String fullName;
    private final boolean enabled;

    static TestUserBuilder builder() {
        return new TestUserBuilder();
    }

    public static final TestUser ADMIN = TestUser.builder()
            .id(1L)
            .email("admin@gmail.com")
            .fullName("Admin")
            .enabled(true)
            .build();


    public static final TestUser USER_MANAGER = TestUser.builder()
            .id(2L)
            .email("user_manager@gmail.com")
            .fullName("User Manager")
            .enabled(true)
            .build();

    public static final TestUser USER = TestUser.builder()
            .id(3L)
            .email("regular_user@gmail.com")
            .fullName("Regular User")
            .enabled(true)
            .build();

    public static final TestUser NO_MY_MEAL = TestUser.builder()
            .id(4L)
            .email("no_my_meal@gmail.com")
            .fullName("No My Meal")
            .enabled(true)
            .build();

    public static final TestUser NO_USER_MANAGEMENT = TestUser.builder()
            .id(5L)
            .email("no_user_managent@gmail.com")
            .fullName("No User Management")
            .enabled(true)
            .build();

    public static final TestUser NO_MEAL_MANAGEMENT = TestUser.builder()
            .id(6L)
            .email("no_meal_managent@gmail.com")
            .fullName("No Meal Management")
            .enabled(true)
            .build();

    public static final TestUser ONLY_USER_MANAGEMENT = TestUser.builder()
            .id(7L)
            .email("only_user_management@gmail.com")
            .fullName("Only User Management")
            .enabled(true)
            .build();

    public static class TestUserBuilder {
        private long id;
        private String email;
        private String fullName;
        private boolean enabled;

        TestUserBuilder() {
        }

        public TestUser.TestUserBuilder id(final long id) {
            this.id = id;
            return this;
        }

        public TestUser.TestUserBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public TestUser.TestUserBuilder fullName(final String fullName) {
            this.fullName = fullName;
            return this;
        }

        public TestUser.TestUserBuilder enabled(final boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public TestUser build() {
            return new TestUser(this.id, this.email, this.fullName, this.enabled);
        }

    }
}
