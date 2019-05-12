package com.mealtracker.domains;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UserTest {

    @Test
    public void isEnabled_UserDeleted_ExpectDisabled() {
        var user = new User();
        user.setDeleted(true);
        Assertions.assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void isEnabled_UserNotDeleted_ExpectEnabled() {
        var user = new User();
        user.setDeleted(true);
        Assertions.assertThat(user.isEnabled()).isTrue();
    }
}
