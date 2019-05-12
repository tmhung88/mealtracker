package com.mealtracker.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JwtTokenValidator {
    private static final List<String> COMMON_MISSING_VALUES = Arrays.asList(null, "undefined", "null", "");
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String EMPTY_JWT_CLAIM_ERROR = "JWT claims string cannot be empty";
    private static final String INVALID_TOKEN_TYPE = "Token must be be a Bearer token";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private final Map<Class, String> exceptionMessageMapping;
    private final String jwtSecretKey;

    public JwtTokenValidator(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
        exceptionMessageMapping = new HashMap<>();
        exceptionMessageMapping.put(SignatureException.class, "Invalid JWT signature");
        exceptionMessageMapping.put(MalformedJwtException.class, "Invalid JWT token");
        exceptionMessageMapping.put(ExpiredJwtException.class, "Expired JWT token");
        exceptionMessageMapping.put(UnsupportedJwtException.class, "Unsupported JWT token");
        exceptionMessageMapping.put(IllegalArgumentException.class, EMPTY_JWT_CLAIM_ERROR);
    }


    public void validate(String authToken) throws JwtValidationException {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authToken);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                UnsupportedJwtException | IllegalArgumentException ex) {
            throw new JwtValidationException(exceptionMessageMapping.get(ex.getClass()), ex);
        }
    }

    public Optional<String> extract(HttpServletRequest request) {
        var bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (COMMON_MISSING_VALUES.contains(bearerToken)) {
            return Optional.empty();
        }

        var isBearerToken = bearerToken.startsWith(BEARER_TOKEN_PREFIX);
        if (!isBearerToken) {
            throw new JwtValidationException(INVALID_TOKEN_TYPE);
        }
        var jwt = bearerToken.substring(BEARER_TOKEN_PREFIX.length());
        if (COMMON_MISSING_VALUES.contains(jwt)) {
            return Optional.empty();
        }

        return Optional.of(jwt);
    }

}
