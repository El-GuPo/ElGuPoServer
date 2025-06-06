package com.elgupo.elguposerver.database.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikeEventRequest {

    Long eventId;

    Long userId;

    Long catId;
}
