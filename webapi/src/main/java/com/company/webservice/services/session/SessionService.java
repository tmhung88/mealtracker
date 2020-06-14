package com.company.webservice.services.session;

import com.company.webservice.security.jwt.JwtTokenProvider;
import com.company.webservice.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SessionService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    public AccessToken generateToken(SessionInput sessionInput) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                sessionInput.getEmail(),
                sessionInput.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var token = jwtTokenProvider.generateToken(authentication);
        return AccessToken.jwt(token);
    }
}
