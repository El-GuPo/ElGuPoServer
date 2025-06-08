package com.elgupo.elguposerver.database.routes;

import com.elgupo.elguposerver.database.models.LikeEventRequest;
import com.elgupo.elguposerver.database.models.LikeEventResponse;
import com.elgupo.elguposerver.database.services.LikeEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class LikeEventController {

    private final LikeEventService likeEventService;

    @Autowired
    public LikeEventController(LikeEventService likeEventService) {
        this.likeEventService = likeEventService;
    }

    @PostMapping("/like_event")
    public ResponseEntity<LikeEventResponse> likeEvent(@RequestBody LikeEventRequest likeEventRequest) {
        log.info("Like event: {}", likeEventRequest);
        return ResponseEntity.ok(likeEventService.likeEvent(likeEventRequest));
    }

    @GetMapping("/like_events/user/{userId}/events")
    public List<Long> getLikedEvents(@PathVariable Long userId) {
        return likeEventService.getLikedEvents(userId);
    }

    @GetMapping("/like_events/user/{userId1}/common_events/{userId2}")
    public List<Long> getCommonEvents(@PathVariable Long userId1, @PathVariable Long userId2) {
        return likeEventService.getCommonEvents(userId1, userId2);
    }

    @GetMapping("/like_events/user/{userId}/cats")
    public Set<Long> getCats(@PathVariable Long userId) {
        return likeEventService.getCats(userId);
    }

    @GetMapping("/like_events/event/{eventId}/get_users")
    public List<Long> getUsers(@PathVariable Long eventId) {
        return likeEventService.getUsers(eventId);
    }

    @GetMapping("/like_events/user/{userId}/get_count/{catId}")
    public Long getCountByUserIdAndCatId(@PathVariable Long userId, @PathVariable Long catId) {
        return likeEventService.getCountByUserIdAndCatId(userId, catId);
    }
}
