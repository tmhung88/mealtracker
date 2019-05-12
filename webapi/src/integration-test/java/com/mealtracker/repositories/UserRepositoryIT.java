package com.mealtracker.repositories;

import com.mealtracker.domains.Role;
import com.mealtracker.domains.User;
import com.mealtracker.domains.UserSettings;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")

public class UserRepositoryIT {

    @ClassRule
    public static MySQLContainer mySQLContainer = AppDbContainer.getInstance();

    @Autowired
    private UserRepository userRepository;

    @Test
    public void simpleTest() {
        User user = new User();
        user.setEmail("abc@gmail.com");
        user.setFullName("Hello World");
        user.setRole(Role.ADMIN);
        user.setEncryptedPassword("asdfkl;jdashfd");

        var settings = new UserSettings();
        settings.setDailyCalorieLimit(100);
        user.setUserSettings(settings);
        userRepository.save(user);


        Assertions.assertThat(userRepository.findByEmail("abc@gmail.com").get())
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("fullName", user.getFullName())
                .hasFieldOrPropertyWithValue("role", user.getRole())
                .hasFieldOrPropertyWithValue("encryptedPassword", user.getEncryptedPassword())
                .hasFieldOrPropertyWithValue("enabled", user.isEnabled());
    }

}
