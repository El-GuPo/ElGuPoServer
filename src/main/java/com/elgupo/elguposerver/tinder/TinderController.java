package com.elgupo.elguposerver.tinder;

import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.services.LikeEventService;
import com.elgupo.elguposerver.dataclasses.Event;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@RestController
public class TinderController {
    @GetMapping("/candidates-list")
    public List<User> getCandidatesList(
            @RequestParam Long mainUserId,
            @RequestParam Long eventId,
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge,
            @RequestParam String sex
    ) {
        return TinderService.getCandidates(mainUserId, eventId, minAge, maxAge, sex);
    }
    @GetMapping("/get-matches")
    public List<User> getMatches(
            @RequestParam Long mainUserId,
            @RequestParam Long eventId
    ) {
        return TinderService.getMatches(mainUserId, eventId);
    }
    @GetMapping("/get-liked-events")
    public List<Event> getLikedEvents(Long userId) {
        return TinderService.getLikedEvents(userId);
    }
}
