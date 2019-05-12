package com.mealtracker.security.jwt;

import com.mealtracker.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class JwtAuthenticationHandler {

    private final JwtTokenProvider tokenProvider;
    private final JwtTokenValidator tokenValidator;

    public JwtAuthenticationHandler(JwtTokenProvider tokenProvider, JwtTokenValidator tokenValidator) {
        this.tokenProvider = tokenProvider;
        this.tokenValidator = tokenValidator;
    }

    public Optional<UsernamePasswordAuthenticationToken> authenticate(HttpServletRequest request) {
        var jwtOptional = tokenValidator.extract(request);
        if (jwtOptional.isEmpty()) {
            return Optional.empty();
        }
        String jwtToken = jwtOptional.get();
        tokenValidator.validate(jwtToken);
        var jwtClaims = tokenProvider.getBodyFromJwtToken(jwtToken);
        UserDetails userDetails = UserPrincipal.jwtClaims(jwtClaims);
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return Optional.of(authentication);
    }
}
