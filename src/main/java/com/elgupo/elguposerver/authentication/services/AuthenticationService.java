package com.elgupo.elguposerver.authentication.services;

import com.elgupo.elguposerver.authentication.exceptions.BadEmailException;
import com.elgupo.elguposerver.authentication.exceptions.BadPasswordException;
import com.elgupo.elguposerver.authentication.exceptions.EmailAlreadyInUseException;
import com.elgupo.elguposerver.authentication.exceptions.NotMatchingPasswordsException;
import com.elgupo.elguposerver.authentication.models.LoginRequest;
import com.elgupo.elguposerver.authentication.models.LoginResponse;
import com.elgupo.elguposerver.authentication.models.RegistrationRequest;
import com.elgupo.elguposerver.authentication.models.RegistrationResponse;
import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.repositories.UserRepository;
import com.elgupo.elguposerver.hashing.PasswordHasher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    UserRepository userRepository;

    EmailValidator emailValidator = EmailValidator.getInstance();

    public RegistrationResponse register(RegistrationRequest registrationRequest) {

        if (!emailValidator.isValid(registrationRequest.email)) {
            log.info("Invalid email address: {}", registrationRequest.email);
            throw new BadEmailException();
        }

        if (userRepository.existsByEmail(registrationRequest.email)) {
            log.info("Email already exists");
            throw new EmailAlreadyInUseException();
        }

        if (!registrationRequest.password.equals(registrationRequest.confirmPassword)) {
            log.info("Passwords do not match");
            throw new NotMatchingPasswordsException();
        }

        String salt = PasswordHasher.genSalt();
        String hashedPassword = PasswordHasher.hashPassword(registrationRequest.password, salt);

        User user = userRepository.save(new User(
                registrationRequest.email,
                hashedPassword,
                salt
        ));
        log.info("User registered: {}", user.getEmail());

        return new RegistrationResponse(
                registrationRequest.email,
                user.getId(),
                "OK"
        );
    }

    public LoginResponse login(LoginRequest loginRequest) {

        if (!emailValidator.isValid(loginRequest.email)) {
            log.info("Invalid email address: {}", loginRequest.email);
            throw new BadEmailException();
        }

        User user = userRepository.findByEmail(loginRequest.email);

        if (user == null) {
            log.info("User with email {} not found", loginRequest.email);
            throw new BadEmailException();
        }

        if (user.getPassword().equals(PasswordHasher.hashPassword(loginRequest.password, user.getSalt()))) {
            log.info("Login successful");
            return new LoginResponse(
                    user.getEmail(),
                    user.getId(),
                    user.getName() != null,
                    "OK"
            );
        }
        throw new BadPasswordException();
    }

}