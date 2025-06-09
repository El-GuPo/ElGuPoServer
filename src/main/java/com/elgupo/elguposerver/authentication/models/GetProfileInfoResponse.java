package com.elgupo.elguposerver.authentication.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetProfileInfoResponse {

    public String sex;

    public String name;

    public String surname;

    public Integer age;

    public String description;

    public String telegramTag;

    public String email;
}
