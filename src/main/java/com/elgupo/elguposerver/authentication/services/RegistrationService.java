package com.elgupo.elguposerver.authentication.services;

import com.elgupo.elguposerver.authentication.models.RegistrationResponse;
import com.elgupo.elguposerver.authentication.models.RegistrationRequest;
import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    UserRepository userRepository;

    public RegistrationResponse register(RegistrationRequest registrationRequest) {

        if (userRepository.existsByEmail(registrationRequest.email)) {
            throw new BadCredentialsException("This email address is already in use");
        }

        if (!registrationRequest.password.equals(registrationRequest.confirmPassword)) {
            throw new BadCredentialsException("Passwords do not match");
        }

        String salt = BCrypt.gensalt(12);
        String hashedPassword = BCrypt.hashpw(registrationRequest.password, salt);

        User user = userRepository.save(new User(
                registrationRequest.email,
                hashedPassword,
                salt
        ));

        return new RegistrationResponse(
                registrationRequest.email,
                user.getId()
        );

    }
}
