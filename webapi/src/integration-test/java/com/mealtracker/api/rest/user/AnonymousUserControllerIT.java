package com.mealtracker.api.rest.user;

import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.exceptions.BadRequestAppException;
import com.mealtracker.services.user.AnonymousUserService;
import com.mealtracker.services.user.RegisterUserInput;
import com.mealtracker.services.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.mealtracker.TestError.BAD_SPECIFIC_INPUT;
import static com.mealtracker.request.AppRequestBuilders.post;
import static com.mealtracker.utils.matchers.UserRegistrationInputMatchers.email;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {AnonymousUserController.class})
@ContextConfiguration(classes={MealTrackerApplication.class, WebSecurityConfig.class})
public class AnonymousUserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnonymousUserService anonymousUserService;

    @MockBean
    private UserService userService;


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

        when(anonymousUserService.registerUser(email(input.getEmail())))
                .thenThrow(BadRequestAppException.emailTaken("superman@gmail.com"));

        mockMvc.perform(post("/v1/users").content(input))
                .andExpect(BAD_SPECIFIC_INPUT.httpStatus())
                .andExpect(BAD_SPECIFIC_INPUT.json("Email superman@gmail.com is already taken"));
    }

    @Test
    public void registerUser_ValidUser_ExpectUserRegistered() throws Exception {
        var registrationInput = registrationRequest();
        mockMvc.perform(post("/v1/users").content(registrationInput))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User registered successfully'}}"));
    }


    RegisterUserInput registrationRequest() {
        var request =  new RegisterUserInput();
        request.setEmail("superman@gmail.com");
        request.setFullName("Superman");
        request.setPassword("JusticeLeague");
        return request;
    }
}
