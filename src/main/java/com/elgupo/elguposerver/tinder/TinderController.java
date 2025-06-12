package com.elgupo.elguposerver.tinder;

import com.elgupo.elguposerver.database.models.User;
import com.elgupo.elguposerver.database.services.LikeEventService;
import com.elgupo.elguposerver.dataclasses.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@RestController
public class TinderController {

    private final TinderService tinderService;

    @Autowired
    public TinderController(TinderService tinderService) {
        this.tinderService = tinderService;
    }

    @GetMapping("/candidates-list")
    public List<User> getCandidatesList(
            @RequestParam Long mainUserId,
            @RequestParam Long eventId,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String sex
    ) {
        return tinderService.getCandidates(mainUserId, eventId, minAge, maxAge, sex);
    }
    @GetMapping("/get-matches")
    public List<User> getMatches(
            @RequestParam Long mainUserId,
            @RequestParam Long eventId
    ) {
        return tinderService.getMatches(mainUserId, eventId);
    }
    @GetMapping("/get-liked-events")
    public List<Event> getLikedEvents(Long userId) {
        return tinderService.getLikedEvents(userId);
    }
}
