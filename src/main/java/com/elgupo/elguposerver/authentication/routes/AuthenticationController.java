package com.elgupo.elguposerver.authentication.routes;

import com.elgupo.elguposerver.authentication.models.LoginRequest;
import com.elgupo.elguposerver.authentication.models.LoginResponse;
import com.elgupo.elguposerver.authentication.models.RegistrationResponse;
import com.elgupo.elguposerver.authentication.models.RegistrationRequest;
import com.elgupo.elguposerver.authentication.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

    @GetMapping("/auth")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

}
