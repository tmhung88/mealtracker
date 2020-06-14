package com.company.webservice.security.jwt;

import com.company.webservice.domains.Privilege;
import com.company.webservice.domains.Role;
import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtAuthenticationHandlerTest {

    private final JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
    private final JwtTokenValidator tokenValidator = mock(JwtTokenValidator.class);
    private final JwtAuthenticationHandler authenticationHandler = new JwtAuthenticationHandler(tokenProvider, tokenValidator);


    @Test
    public void authenticate_RequestWithoutToken_ExpectAnonymous() {
        var request = request();
        when(tokenValidator.extract(request)).thenReturn(Optional.empty());
        Assertions.assertThat(authenticationHandler.authenticate(request)).isEmpty();
    }

    @Test
    public void authenticate_RequestWithInvalidToken_ExpectExceptionPropogated() {
        var request = request();
        var invalidToken = "tjfj;lasjd";
        when(tokenValidator.extract(eq(request))).thenReturn(Optional.of(invalidToken));
        doThrow(new JwtValidationException("Invalid token")).when(tokenValidator).validate(invalidToken);

        assertThatThrownBy(() -> authenticationHandler.authenticate(request)).isInstanceOf(JwtValidationException.class);
    }

    @Test
    public void authenticate_RequestWithValidToken_ExpectAuthenticationReturned() {
        var request = request();
        var validToken = "Valid jwt token";
        var validClaims = validJwtClaims();
        when(tokenValidator.extract(request)).thenReturn(Optional.of(validToken));
        when(tokenProvider.getBodyFromJwtToken(validToken)).thenReturn(validClaims);

        Assertions.assertThat(authenticationHandler.authenticate(request)).isNotEmpty();

    }

    HttpServletRequest request() {
        return mock(HttpServletRequest.class);
    }

    private Claims validJwtClaims() {
        return new UserPrincipalClaimsBuilder()
                .id(15)
                .fullName("Hung")
                .role(Role.REGULAR_USER)
                .email("hello@hung.com").privileges(Privilege.MY_MEALS).build();
    }
}
