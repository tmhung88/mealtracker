package com.company.webservice.security.jwt;

import com.company.webservice.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class JwtTokenProvider {

    private final String jwtSecret;

    public JwtTokenProvider(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String generateToken(Authentication authentication) {
        var userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Jwts.builder()
                .setClaims(userPrincipal.toJwtClaims())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Claims getBodyFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
