package com.elgupo.elguposerver.authentication.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponse {
    public String email;

    public int id;

    public boolean isProfileFilledOut;

    public String message;
}
