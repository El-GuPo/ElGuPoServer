package com.elgupo.elguposerver.authentication.routes;

import com.elgupo.elguposerver.authentication.models.*;
import com.elgupo.elguposerver.authentication.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

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

    @PostMapping("/fill_profile")
    public ResponseEntity<FillProfileResponse> fillProfile(@RequestBody FillProfileRequest fillProfileRequest) {
        return ResponseEntity.ok(authenticationService.fillProfile(fillProfileRequest));
    }

    @GetMapping("/get_profile/{userId}")
    public ResponseEntity<GetProfileInfoResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(authenticationService.getProfile(userId));
    }
}
