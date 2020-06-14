package com.company.webservice.api.rest;

import com.company.webservice.payloads.SuccessEnvelop;
import com.company.webservice.payloads.session.SessionResponse;
import com.company.webservice.services.session.SessionInput;
import com.company.webservice.services.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sessions")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public SuccessEnvelop<SessionResponse> generateToken(@RequestBody SessionInput sessionInput) {
        var accessToken = sessionService.generateToken(sessionInput);
        return SessionResponse.envelop(accessToken);
    }
}
