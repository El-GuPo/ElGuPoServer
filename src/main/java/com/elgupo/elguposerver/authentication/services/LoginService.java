package com.elgupo.elguposerver.authentication.services;

import com.elgupo.elguposerver.authentication.models.LoginRequest;
import com.elgupo.elguposerver.authentication.models.LoginResponse;
import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    public LoginResponse login(LoginRequest loginRequest) {

        if (!userRepository.existsByEmail(loginRequest.email)) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(loginRequest.email);

        if (user.getPassword().equals(BCrypt.hashpw(loginRequest.password, user.getSalt()))) {
            return new LoginResponse(
                user.getEmail(),
                    user.getId(),
                    false
            );
        }
        throw new BadCredentialsException("Invalid email or password");
    }
}
