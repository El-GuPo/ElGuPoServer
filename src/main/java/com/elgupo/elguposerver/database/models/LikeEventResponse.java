package com.elgupo.elguposerver.database.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeEventResponse {
    private Long userId;
    private Long eventId;
    private Long catId;
    private String status;
}
