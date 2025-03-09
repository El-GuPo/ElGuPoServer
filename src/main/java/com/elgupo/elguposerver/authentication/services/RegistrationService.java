package com.elgupo.elguposerver.authentication.services;

import com.elgupo.elguposerver.authentication.models.RegistrationResponse;
import com.elgupo.elguposerver.authentication.models.RegistrationRequest;
import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.repositories.UserRepository;
import com.elgupo.elguposerver.hashing.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    UserRepository userRepository;

    public RegistrationResponse register(RegistrationRequest registrationRequest) {

        if (userRepository.existsByEmail(registrationRequest.email)) {
            logger.info("Email already exists");
            throw new BadCredentialsException("This email address is already in use");
        }

        if (!registrationRequest.password.equals(registrationRequest.confirmPassword)) {
            logger.info("Passwords do not match");
            throw new BadCredentialsException("Passwords do not match");
        }

        String salt = PasswordHasher.genSalt();
        String hashedPassword = PasswordHasher.hashPassword(registrationRequest.password, salt);

        User user = userRepository.save(new User(
                registrationRequest.email,
                hashedPassword,
                salt
        ));
        logger.info("User registered: {}", user.getEmail());

        return new RegistrationResponse(
                registrationRequest.email,
                user.getId()
        );

    }
}
