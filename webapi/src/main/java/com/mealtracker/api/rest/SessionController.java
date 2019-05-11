package com.mealtracker.api.rest;

import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.user.SessionResponse;
import com.mealtracker.security.jwt.JwtTokenProvider;
import com.mealtracker.services.user.SessionInput;
import com.mealtracker.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sessions")
public class SessionController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping
    public SuccessEnvelop<SessionResponse> authenticate(@RequestBody SessionInput sessionInput) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                sessionInput.getEmail(),
                sessionInput.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtAccessToken = jwtTokenProvider.generateToken(authentication);
        return SessionResponse.envelop(jwtAccessToken);
    }

}
