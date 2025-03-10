package com.elgupo.elguposerver.authentication.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public class RegistrationRequest {

    @NotBlank
    @Email
    public String email;

    @NotBlank
    public String password;

    @NotBlank
    public String confirmPassword;

}
