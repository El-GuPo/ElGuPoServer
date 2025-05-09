package com.elgupo.elguposerver.authentication.routes;

import com.elgupo.elguposerver.authentication.models.*;
import com.elgupo.elguposerver.authentication.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/check_email")
    public ResponseEntity<CheckEmailResponse> checkEmail(@RequestBody CheckEmailRequest checkEmailRequest) {
        log.info("Check email request: {}", checkEmailRequest);
        return ResponseEntity.ok(authenticationService.checkEmail(checkEmailRequest));
    }

}
