package com.elgupo.elguposerver.database.services;

import com.elgupo.elguposerver.database.models.LikeEventEntry;
import com.elgupo.elguposerver.database.models.LikeEventRequest;
import com.elgupo.elguposerver.database.models.LikeEventResponse;
import com.elgupo.elguposerver.database.repositories.LikeEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LikeEventService {

    @Autowired
    private LikeEventRepository likeEventRepository;

    public LikeEventResponse likeEvent(LikeEventRequest likeEventRequest) {
        LikeEventEntry likeEventEntry = new LikeEventEntry(
                likeEventRequest.getUserId(),
                likeEventRequest.getEventId()
        );
        likeEventRepository.save(likeEventEntry);
        return new LikeEventResponse();
    }

    public List<Long> getLikedEvents(Long userId) {
        List<LikeEventEntry> likeEventEntries = likeEventRepository.findByUserId(userId);
        return likeEventEntries.stream().map(LikeEventEntry::getEventId).collect(Collectors.toList());
    }

    public List<Long> getCommonEvents(Long userId1, Long userId2) {
        return likeEventRepository.findCommonEventsByUsersId(userId1, userId2);
    }
}
