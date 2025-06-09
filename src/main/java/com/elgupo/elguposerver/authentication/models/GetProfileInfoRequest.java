package com.elgupo.elguposerver.authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetProfileInfoRequest {
    public Long userId;
}
