package com.mealtracker.api.rest;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.User;
import com.mealtracker.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserController.class})
@ContextConfiguration(classes={MealTrackerApplication.class, WebSecurityConfig.class})
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void findByEmail_ExistingEmail_ExpectResultReturn() throws Exception {
        User user = new User();
        user.setEmail("superman@gmail.com");
        user.setFullName("Superman");
        when(userService.findByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));

        mockMvc.perform(get(String.format("/v1/users?email=%s", user.getEmail())))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'fullName':'Superman','email':'superman@gmail.com'}}"));
    }

    @Test
    public void findByEmail_UnknownEmail_ExpectNoResultReturn() throws Exception {
        String unknownEmail = "unknownEmail";
        when(userService.findByEmail(eq(unknownEmail))).thenReturn(Optional.empty());

        mockMvc.perform(get(String.format("/v1/users?email=%s", unknownEmail)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(content().json("{'error':{'code':40400,'message':'User not found with the given email'}}"));
    }
}
