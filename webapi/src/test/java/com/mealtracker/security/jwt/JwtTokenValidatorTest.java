package com.mealtracker.security.jwt;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class JwtTokenValidatorTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenValidator validator = new JwtTokenValidator("SecretKey");

    @Test
    public void extract_AuthorizationHeaderMissing_ExpectEmptyReturned() {
        assertThat(validator.extract(request(null))).isEmpty();
    }

    @Test
    public void extract_AuthorizationHeaderAsNullString_ExpectEmptyReturned() {
        assertThat(validator.extract(request("null"))).isEmpty();
    }

    @Test
    public void extract_AuthorizationHeaderAsUndefinedString_ExpectEmptyReturned() {
        assertThat(validator.extract(request("undefined"))).isEmpty();
    }

    @Test
    public void extract_TokenIsNotBearer_ExpectException() {
        Assertions.assertThatThrownBy(() -> validator.extract(request("ABCTYPE sdafopdsfyuy53")))
                .isInstanceOf(JwtValidationException.class).hasMessage("Token must be be a Bearer token");
    }

    @Test
    public void extract_JwtTokenIsMissing_ExpectEmpty() {
        assertThat(validator.extract(request("Bearer "))).isEmpty();
    }

    @Test
    public void extract_AuthorizationHeaderWithJwtToken_ExpectTokenReturned() {
        var token = "54523dsjifhadsoh35";
        assertThat(validator.extract(request("Bearer " + token))).hasValue(token);
    }

    HttpServletRequest request(String authorizationHeaderValue) {
        var request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(authorizationHeaderValue);
        return request;
    }
}
