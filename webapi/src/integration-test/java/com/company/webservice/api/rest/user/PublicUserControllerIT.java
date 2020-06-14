package com.company.webservice.api.rest.user;

import com.company.webservice.WebServiceApplication;
import com.company.webservice.api.rest.PublicUserController;
import com.company.webservice.config.WebSecurityConfig;
import com.company.webservice.domains.User;
import com.company.webservice.exceptions.BadRequestAppException;
import com.company.webservice.request.AppRequestBuilders;
import com.company.webservice.services.user.PublicUserService;
import com.company.webservice.services.user.RegisterUserInput;
import com.company.webservice.services.user.UserService;
import com.company.webservice.utils.matchers.UserRegistrationInputMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.company.webservice.TestError.BAD_SPECIFIC_INPUT;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {PublicUserController.class})
@ContextConfiguration(classes={WebServiceApplication.class, WebSecurityConfig.class})
public class PublicUserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicUserService publicUserService;

    @MockBean
    private UserService userService;

    @Test
    public void registerUser_AnonymousWithValidInput_ExpectUserRegistered() throws Exception {
        var registrationInput = registrationRequest();
        mockMvc.perform(AppRequestBuilders.post("/v1/users").content(registrationInput))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User registered successfully'}}"));
    }

    @Test
    public void registerUser_InvalidEmail_ExpectEmailValidationErrors() throws Exception {
        String invalidEmail = "abc";
        var input = registrationRequest();
        input.setEmail(invalidEmail);

        mockMvc.perform(AppRequestBuilders.post("/v1/users").content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'email','message':'size must be between 5 and 200'},{'name':'email','message':'must be a well-formed email address'}]}}"));
    }

    @Test
    public void registerUser_InvalidFullName_ExpectFullNameValidationErrors() throws Exception {
        String invalidFullName = "aa";
        var input = registrationRequest();
        input.setFullName(invalidFullName);

        mockMvc.perform(AppRequestBuilders.post("/v1/users").content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'fullName','message':'size must be between 5 and 200'}]}}"));
    }

    @Test
    public void registerUser_InvalidPassword_ExpectFullNameValidationErrors() throws Exception {
        String invalidPassword = "d";
        var input = registrationRequest();
        input.setPassword(invalidPassword);

        mockMvc.perform(AppRequestBuilders.post("/v1/users").content(input))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':{'code':40000,'message':'Bad Input','errorFields':[{'name':'password','message':'size must be between 5 and 100'}]}}"));
    }


    @Test
    public void registerUser_ExistingEmail_ExpectError() throws Exception {
        var input = registrationRequest();

        when(publicUserService.registerUser(UserRegistrationInputMatchers.email(input.getEmail())))
                .thenThrow(BadRequestAppException.emailTaken("superman@gmail.com"));

        mockMvc.perform(AppRequestBuilders.post("/v1/users").content(input))
                .andExpect(BAD_SPECIFIC_INPUT.httpStatus())
                .andExpect(BAD_SPECIFIC_INPUT.json("Email superman@gmail.com is already taken"));
    }

    @Test
    public void registerUser_ValidUser_ExpectUserRegistered() throws Exception {
        var registrationInput = registrationRequest();
        mockMvc.perform(AppRequestBuilders.post("/v1/users").content(registrationInput))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'message':'User registered successfully'}}"));
    }

    @Test
    public void getUser_ExistingUser_ExpectPublicUserInfoReturned() throws Exception {
        var user = userWithCompleteDetails();
        when(publicUserService.getByEmail(user.getEmail())).thenReturn(user);
        mockMvc.perform(get("/v1/users?email=" + user.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'fullName':'Hello World','email':'hello@gmail.com'}}"));
    }


    RegisterUserInput registrationRequest() {
        var request =  new RegisterUserInput();
        request.setEmail("superman@gmail.com");
        request.setFullName("Superman");
        request.setPassword("JusticeLeague");
        return request;
    }

    User userWithCompleteDetails() {
        var user = new User();
        user.setEmail("hello@gmail.com");
        user.setFullName("Hello World");
        user.setEncryptedPassword("4895742");
        user.setId(5L);
        user.setDeleted(false);
        return user;
    }

}
