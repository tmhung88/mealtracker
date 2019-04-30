package com.mealtracker.api.rest;

import com.mealtracker.JwtToken;
import com.mealtracker.MealTrackerApplication;
import com.mealtracker.config.WebSecurityConfig;
import com.mealtracker.domains.User;
import com.mealtracker.exceptions.BadRequestException;
import com.mealtracker.payloads.UserRegistrationRequest;
import com.mealtracker.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.mealtracker.TestUtils.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'error':{'code':40400,'message':'User not found with the given email'}}"));
    }

    @Test
    public void registerUser_InvalidEmail_ExpectEmailValidationErrors() throws Exception {
        String invalidEmail = "abc";
        var request = validRegistrationRequest();
        request.setEmail(invalidEmail);

        mockMvc.perform(
                post("/v1/users")
                        .content(json(request)).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Invalid Input','errorFields':[{'name':'email','message':'size must be between 5 and 200'},{'name':'email','message':'must be a well-formed email address'}]}}"));
    }

    @Test
    public void registerUser_InvalidFullName_ExpectFullNameValidationErrors() throws Exception {
        String invalidFullName = "aa";
        var request = validRegistrationRequest();
        request.setFullName(invalidFullName);

        mockMvc.perform(
                post("/v1/users")
                        .content(json(request)).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Invalid Input','errorFields':[{'name':'fullName','message':'size must be between 5 and 200'}]}}"));
    }

    @Test
    public void registerUser_InvalidPassword_ExpectFullNameValidationErrors() throws Exception {
        String invalidPassword = "d";
        var request = validRegistrationRequest();
        request.setPassword(invalidPassword);

        mockMvc.perform(
                post("/v1/users")
                        .content(json(request)).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Invalid Input','errorFields':[{'name':'password','message':'size must be between 5 and 100'}]}}"));
    }



    @Test
    public void registerUser_ExistingEmail_ExpectError() throws Exception {
        var request = validRegistrationRequest();

        when(userService.registerUser(any(User.class))).thenThrow(new BadRequestException("Email superman@gmail.com is already taken"));

        mockMvc.perform(
                post("/v1/users")
                        .content(json(request)).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Email superman@gmail.com is already taken'}}"));
    }

    @Test
    public void registerUser_ValidUser_ExpectUserCreated() throws Exception {
        var registrationRequest = validRegistrationRequest();
        mockMvc.perform(post("/v1/users").content(json(registrationRequest)).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User registered successfully'}}"));
    }

    @Test
    public void listUsers_Anonymous_ExpectAuthenticationError() throws Exception {
        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{'error':{'code':40100,'message':'No authentication token found. Please look at this document'}}"));
    }

    @Test
    public void listUsers_NonAdminUser_ExpectAuthorizationError() throws Exception {
        mockMvc.perform(get("/v1/users").header("Authorization", JwtToken.REGULAR_USER))
                .andExpect(status().isForbidden())
                .andExpect(content().json("{'error':{'code':40300,'message':'You are not allowed to use this api'}}"));
    }

    UserRegistrationRequest validRegistrationRequest() {
        var request =  new UserRegistrationRequest();
        request.setEmail("superman@gmail.com");
        request.setFullName("Superman");
        request.setPassword("JusticeLeague");
        return request;
    }
}
