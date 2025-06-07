package com.elgupo.elguposerver.database.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetLikeResponse {
    private boolean isExists;
    private boolean isLiked;
}
