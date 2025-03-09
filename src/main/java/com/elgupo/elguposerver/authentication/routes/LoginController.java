package com.elgupo.elguposerver.authentication.routes;

import com.elgupo.elguposerver.authentication.models.LoginRequest;
import com.elgupo.elguposerver.authentication.models.LoginResponse;
import com.elgupo.elguposerver.authentication.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/auth")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(loginService.login(loginRequest));
    }
}
