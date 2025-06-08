package com.elgupo.elguposerver.database.routes;

import com.elgupo.elguposerver.database.models.GetLikeResponse;
import com.elgupo.elguposerver.database.models.LikeUserRequest;
import com.elgupo.elguposerver.database.services.LikeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class LikeUserController {

    private final LikeUserService likeUserService;

    @Autowired
    public LikeUserController(LikeUserService likeUserService) {
        this.likeUserService = likeUserService;
    }

    @PostMapping("/like_user")
    public ResponseEntity<?> likeUser(@RequestBody LikeUserRequest likeUserRequest) {
        return ResponseEntity.ok(likeUserService.likeUser(likeUserRequest));
    }

    @GetMapping("/like_users/get_like")
    public ResponseEntity<GetLikeResponse> getLike(@RequestParam Long likerId, @RequestParam Long likeableId, @RequestParam Long eventId) {
        return ResponseEntity.ok(likeUserService.getLike(likerId, likeableId, eventId));
    }

    @GetMapping("/like_users/get_dislike")
    public boolean wasDislike(@RequestParam Long user1, @RequestParam Long user2) {
        return likeUserService.wasDislike(user1, user2);
    }

    @GetMapping("/like_users/get_likes")
    public List<Long> getLikes(@RequestParam Long user, @RequestParam Long event, @RequestParam boolean like) {
        return likeUserService.getLikes(user, event, like);
    }

    @DeleteMapping("/like_users/delete/{eventId}")
    public boolean deleteLikeUsers(@PathVariable Long eventId) {
        return likeUserService.deleteLikeUsers(eventId);
    }

    @GetMapping("like_users/distinct_events")
    public List<Long> getDistinctEvents() {
        return likeUserService.getDistinctEvents();
    }
}
