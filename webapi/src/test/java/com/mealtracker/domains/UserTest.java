package com.mealtracker.domains;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {


    @Test
    public void isEnabled_UserDeleted_ExpectDisabled() {
        var user = new User();
        user.setDeleted(true);
        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void isEnabled_UserNotDeleted_ExpectEnabled() {
        var user = new User();
        user.setDeleted(false);
        assertThat(user.isEnabled()).isTrue();
    }
}
