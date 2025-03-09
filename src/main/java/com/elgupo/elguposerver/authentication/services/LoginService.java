package com.elgupo.elguposerver.authentication.services;

import com.elgupo.elguposerver.authentication.models.LoginRequest;
import com.elgupo.elguposerver.authentication.models.LoginResponse;
import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.repositories.UserRepository;
import com.elgupo.elguposerver.hashing.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    UserRepository userRepository;

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email);

        if (user == null) {
            logger.info("User with email {} not found", loginRequest.email);
            throw new BadCredentialsException("Invalid email or password");
        }

        if (user.getPassword().equals(PasswordHasher.hashPassword(loginRequest.password, user.getSalt()))) {
            logger.info("Login successful");
            return new LoginResponse(
                    user.getEmail(),
                    user.getId(),
                    user.getName() != null
            );
        }
        throw new BadCredentialsException("Invalid email or password");
    }
}
