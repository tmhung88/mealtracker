package com.company.webservice.api.rest;

import com.company.webservice.WebServiceApplication;
import com.company.webservice.config.WebSecurityConfig;
import com.company.webservice.request.AppRequestBuilders;
import com.company.webservice.services.session.AccessToken;
import com.company.webservice.services.session.SessionInput;
import com.company.webservice.services.session.SessionService;
import com.company.webservice.services.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {SessionController.class})
@ContextConfiguration(classes={WebServiceApplication.class, WebSecurityConfig.class})
public class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private UserService userService;

    @Test
    public void generateToken_Anonymous_ValidCredential_ExpectTokenReturned() throws Exception {
        var credentials = validInput();
        when(sessionService.generateToken(eq(credentials))).thenReturn(AccessToken.jwt("<your token>"));

        mockMvc.perform(AppRequestBuilders.post("/v1/sessions").content(credentials))
                .andExpect(status().isOk())
                .andExpect(content().json("{'data':{'accessToken':'<your token>','tokenType':'Bearer'}}"));
    }

    SessionInput validInput() {
        var input = new SessionInput();
        input.setEmail("helloworld@gmail.com");
        input.setPassword("tooStrongPassword");
        return input;
    }



    private static SessionInput eq(SessionInput expectation) {
        return argThat(new SessionInputMatcher(expectation));
    }

    static class SessionInputMatcher implements ArgumentMatcher<SessionInput> {

        private final SessionInput expectation;

        public SessionInputMatcher(SessionInput expectation) {
            this.expectation = expectation;
        }

        @Override
        public boolean matches(SessionInput actual) {
            return expectation.getEmail().equals(actual.getEmail()) && expectation.getPassword().equals(actual.getPassword());
        }
    }
}
