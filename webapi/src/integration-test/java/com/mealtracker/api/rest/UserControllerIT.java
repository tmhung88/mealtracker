package com.mealtracker.api.rest;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.User;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.payloads.UserRegistrationRequest;
import com.mealtracker.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.mealtracker.TestError.AUTHENTICATION_MISSING_TOKEN;
import static com.mealtracker.TestError.AUTHORIZATION_API_ACCESS_DENIED;
import static com.mealtracker.TestError.BAD_SPECIFIC_INPUT;
import static com.mealtracker.TestError.RESOURCE_DATA_NOT_IN_DB;
import static com.mealtracker.TestUser.USER;
import static com.mealtracker.request.AppRequestBuilders.get;
import static com.mealtracker.request.AppRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
    public void findUserByEmail_ExistingEmail_ExpectResultReturn() throws Exception {
        User user = new User();
        user.setEmail("superman@gmail.com");
        user.setFullName("Superman");
        when(userService.findByEmail(eq(user.getEmail()))).thenReturn(Optional.of(user));

        mockMvc.perform(get(String.format("/v1/users?email=%s", user.getEmail())))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'fullName':'Superman','email':'superman@gmail.com'}}"));
    }

    @Test
    public void findUserByEmail_UnknownEmail_ExpectNoResultReturn() throws Exception {
        String unknownEmail = "unknownEmail";
        when(userService.findByEmail(eq(unknownEmail))).thenReturn(Optional.empty());

        mockMvc.perform(get(String.format("/v1/users?email=%s", unknownEmail)))
                .andExpect(RESOURCE_DATA_NOT_IN_DB.httpStatus())
                .andExpect(RESOURCE_DATA_NOT_IN_DB.json("user"));
    }

    @Test
    public void registerUser_InvalidEmail_ExpectEmailValidationErrors() throws Exception {
        String invalidEmail = "abc";
        var input = registrationRequest();
        input.setEmail(invalidEmail);

        mockMvc.perform(post("/v1/users").content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'email','message':'size must be between 5 and 200'},{'name':'email','message':'must be a well-formed email address'}]}}"));
    }

    @Test
    public void registerUser_InvalidFullName_ExpectFullNameValidationErrors() throws Exception {
        String invalidFullName = "aa";
        var input = registrationRequest();
        input.setFullName(invalidFullName);

        mockMvc.perform(post("/v1/users").content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'fullName','message':'size must be between 5 and 200'}]}}"));
    }

    @Test
    public void registerUser_InvalidPassword_ExpectFullNameValidationErrors() throws Exception {
        String invalidPassword = "d";
        var input = registrationRequest();
        input.setPassword(invalidPassword);

        mockMvc.perform(post("/v1/users").content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'password','message':'size must be between 5 and 100'}]}}"));
    }



    @Test
    public void registerUser_ExistingEmail_ExpectError() throws Exception {
        var input = registrationRequest();

        when(userService.registerUser(any(User.class))).thenThrow(BadRequestAppException.emailTaken("superman@gmail.com"));

        mockMvc.perform(post("/v1/users").content(input))
                .andExpect(BAD_SPECIFIC_INPUT.httpStatus())
                .andExpect(BAD_SPECIFIC_INPUT.json("Email superman@gmail.com is already taken"));
    }

    @Test
    public void registerUser_ValidUser_ExpectUserCreated() throws Exception {
        var registrationInput = registrationRequest();
        mockMvc.perform(post("/v1/users").content(registrationInput))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User registered successfully'}}"));
    }

    @Test
    public void listUsers_Anonymous_ExpectAuthenticationError() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andExpect(AUTHENTICATION_MISSING_TOKEN.httpStatus())
                .andExpect(AUTHENTICATION_MISSING_TOKEN.json());
    }

    @Test
    public void listUsers_NonAdminUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/users").auth(USER))
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.httpStatus())
                .andExpect(AUTHORIZATION_API_ACCESS_DENIED.json());
    }

    UserRegistrationRequest registrationRequest() {
        var request =  new UserRegistrationRequest();
        request.setEmail("superman@gmail.com");
        request.setFullName("Superman");
        request.setPassword("JusticeLeague");
        return request;
    }
}
