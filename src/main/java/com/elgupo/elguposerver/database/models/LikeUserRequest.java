package com.elgupo.elguposerver.database.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikeUserRequest {

    Long likerId;

    Long userLikeableId;

    Long eventId;

    boolean isLiked;
}
