package com.elgupo.elguposerver.database.services;

import com.elgupo.elguposerver.database.models.GetLikeResponse;
import com.elgupo.elguposerver.database.models.LikeUserEntry;
import com.elgupo.elguposerver.database.models.LikeUserRequest;
import com.elgupo.elguposerver.database.models.LikeUserResponse;
import com.elgupo.elguposerver.database.repositories.LikeEventRepository;
import com.elgupo.elguposerver.database.repositories.LikeUserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LikeUserService {

    private final LikeUserRepository likeUserRepository;
    private final LikeEventService likeEventService;

    @Autowired
    public LikeUserService(LikeEventService likeEventService, LikeUserRepository likeUserRepository) {
        this.likeEventService = likeEventService;
        this.likeUserRepository = likeUserRepository;
    }

    public LikeUserResponse likeUser(LikeUserRequest likeUserRequest) {
        log.debug("Like user request: {}", likeUserRequest);

        LikeUserEntry likeUserEntry = new LikeUserEntry(
                likeUserRequest.getUserLikeableId(),
                likeUserRequest.getEventId(),
                likeUserRequest.getLikerId(),
                likeUserRequest.isLiked()
        );

        likeUserRepository.save(likeUserEntry);

        boolean match = likeUserRequest.isLiked() &&
                getLike(likeUserRequest.getUserLikeableId(),
                        likeUserRequest.getLikerId(),
                        likeUserRequest.getEventId()).isLiked() &&
                likeEventService.getLikedEvents(likeUserRequest.getUserLikeableId())
                        .contains(likeUserRequest.getEventId());

        return new LikeUserResponse(
                likeUserRequest.getLikerId(),
                likeUserRequest.getUserLikeableId(),
                likeUserRequest.getEventId(),
                likeUserRequest.isLiked(),
                match
        );
    }

    public GetLikeResponse getLike(Long likerId, Long likeableId, Long eventId) {
        LikeUserEntry likeUserEntry = likeUserRepository.findByLikerAndUserLikeableAndEventId(likerId, likeableId, eventId);

        if (likeUserEntry == null) {
            return new GetLikeResponse(false, false);
        }
        return new GetLikeResponse(true, likeUserEntry.isLiked());
    }

    public boolean wasDislike(Long user1, Long user2) {
        return likeUserRepository.existsByLikerAndUserLikeableAndLikedFalse(user1, user2) || likeUserRepository.existsByLikerAndUserLikeableAndLikedFalse(user2, user1);
    }

    public List<Long> getLikes(Long user, Long event, boolean like) {
        return likeUserRepository.getLikes(user, event, like);
    }

    @Transactional
    public boolean deleteLikeUsers(Long eventId) {
        if (!likeUserRepository.existsByEventId(eventId)) {
            return false;
        }
        likeUserRepository.deleteAllByEventId(eventId);
        return true;
    }

    public List<Long> getDistinctEvents() {
        return likeUserRepository.findAllDistinctEventIds();
    }
}
