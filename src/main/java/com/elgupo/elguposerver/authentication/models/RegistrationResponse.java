package com.elgupo.elguposerver.authentication.models;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class RegistrationResponse implements Serializable {
    public String email;

    public int id;

    public String message;
}
