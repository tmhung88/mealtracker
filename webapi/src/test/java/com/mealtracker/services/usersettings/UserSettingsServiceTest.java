package com.mealtracker.services.usersettings;

import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import com.mealtracker.repositories.UserRepository;
import com.mealtracker.services.usersettings.MySettingsInput;
import com.mealtracker.services.usersettings.UserSettingsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsServiceTest {
    @InjectMocks
    private UserSettingsService userSettingsService;

    @Mock
    private UserRepository userRepository;


    @Test
    public void getUserSettings_ExistingUser_ExpectUserSettingsReturned() {
        var user = userWithSettings(415);
        when(userRepository.getOne(eq(user.getId()))).thenReturn(user);
        var userSettings = userSettingsService.getUserSettings(user.getId());

        assertThat(userSettings).hasFieldOrPropertyWithValue("dailyCalorieLimit", 415);
    }

    @Test
    public void updateUserSettings_RequestWithDailyCalorieLimit_ExpectCalorieLimitUpdated() {
        var request = updateSettingsRequest(1000);
        var user = userWithSettings(500);
        when(userRepository.getOne(eq(user.getId()))).thenReturn(user);
        when(userRepository.save(calorieLimit(1000))).thenReturn(user);
        var userSettings = userSettingsService.updateUserSettings(user.getId(), request);

        assertThat(userSettings.getDailyCalorieLimit()).isEqualTo(1000);
    }


    @Test
    public void updateUserSettings_RequestWithoutDailyCalorieLimit_ExpectCalorieLimitNotOverridden() {
        var request = updateSettingsRequest(null);
        var user = userWithSettings(400);
        when(userRepository.getOne(eq(user.getId()))).thenReturn(user);
        when(userRepository.save(calorieLimit(400))).thenReturn(user);

        var userSettings = userSettingsService.updateUserSettings(user.getId(), request);

        assertThat(userSettings.getDailyCalorieLimit()).isEqualTo(400);
    }

    private User userWithSettings(int dailyCalorieLimit) {
        var user = new User();
        user.setId(6L);

        var userSettings = new UserSettings();
        userSettings.setDailyCalorieLimit(dailyCalorieLimit);
        user.setUserSettings(userSettings);
        return user;
    }

    private MySettingsInput updateSettingsRequest(Integer calorieLimit) {
        var request = new MySettingsInput();
        request.setDailyCalorieLimit(calorieLimit);
        return request;
    }

    private User calorieLimit(int dailyCalorieLimit) {
        return argThat(new CalorieLimitMatcher(dailyCalorieLimit));
    }

    class CalorieLimitMatcher implements ArgumentMatcher<User> {

        private final int expectedCalorieLimit;

        CalorieLimitMatcher(int expectedCalorieLimit) {
            this.expectedCalorieLimit = expectedCalorieLimit;
        }

        @Override
        public boolean matches(User actual) {
            return expectedCalorieLimit == actual.getUserSettings().getDailyCalorieLimit();
        }
    }
}
