package com.elgupo.elguposerver.database.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeUserResponse {
    private Long likerId;
    private Long userLikeableId;
    private Long eventId;
    private boolean isLiked;
}
