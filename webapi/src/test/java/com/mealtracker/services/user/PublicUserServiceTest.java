package com.mealtracker.services.user;

import com.mealtracker.assertions.AppAssertions;
import com.mealtracker.domains.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PublicUserServiceTest {

    @InjectMocks
    private PublicUserService publicUserService;

    @Mock
    private UserService userService;

    @Test
    public void registerUser_ExpectCompleteUserInfoForUserService() {
        var input = registerUserInput();

        var savedUser = Mockito.mock(User.class);
        when(userService.addUser(UserMatchers.eq(input))).thenReturn(savedUser);
        assertThat(publicUserService.registerUser(input)).isEqualTo(savedUser);
    }

    @Test
    public void getByEmail_UnknownEmail_ExpectException() {
        var unknownEmail = "strange_email@am.com";
        when(userService.findByEmail(unknownEmail)).thenReturn(Optional.empty());
        AppAssertions.assertThatThrownBy(() -> publicUserService.getByEmail(unknownEmail)).hasError(40401, "The given user does not exist");
    }

    RegisterUserInput registerUserInput() {
        var input = new RegisterUserInput();
        input.setEmail("abc@gmail.com");
        input.setFullName("ABC DEF");
        input.setPassword("alphabet");
        return input;
    }

}
