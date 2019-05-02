package com.mealtracker.security.jwt;

import com.mealtracker.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final JwtTokenValidator tokenValidator;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, JwtTokenValidator tokenValidator) {
        this.tokenProvider = tokenProvider;
        this.tokenValidator = tokenValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var jwtOptional = tokenValidator.extract(request);
        if (jwtOptional.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwtToken = jwtOptional.get();
        tokenValidator.validate(jwtToken);
        var jwtClaims = tokenProvider.getBodyFromJwtToken(jwtToken);
        UserDetails userDetails = UserPrincipal.jwtClaims(jwtClaims);
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
