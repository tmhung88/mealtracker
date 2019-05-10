package com.mealtracker.api.rest;

import com.mealtracker.payloads.SuccessEnvelop;
import com.mealtracker.payloads.session.CreateSessionRequest;
import com.mealtracker.payloads.session.CreateSessionResponse;
import com.mealtracker.security.jwt.JwtTokenProvider;
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
    public SuccessEnvelop<CreateSessionResponse> authenticate(@RequestBody CreateSessionRequest createSessionRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                createSessionRequest.getEmail(),
                createSessionRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtAccessToken = jwtTokenProvider.generateToken(authentication);
        return CreateSessionResponse.envelop(jwtAccessToken);
    }

}
