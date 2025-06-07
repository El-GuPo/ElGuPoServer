package com.elgupo.elguposerver.database.repositories;

import com.elgupo.elguposerver.database.models.LikeUserEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LikeUserRepository extends CrudRepository<LikeUserEntry, Integer> {

    LikeUserEntry findByUserLikeable(Long userLikeable);

    LikeUserEntry findByUserLikeableAndLiker(Long userLikeable, Long user);

    boolean existsByUserLikeableAndLiker(Long userLikeable, Long user);

    LikeUserEntry findByLikerAndUserLikeableAndEventId(Long liker, Long user, Long eventId);

    @Query("SELECT CASE WHEN COUNT(ul) > 0 THEN true ELSE false END " +
            "FROM LikeUserEntry ul WHERE ul.liker = :liker AND ul.userLikeable = :userLikeable AND ul.isLiked = false")
    boolean existsByLikerAndUserLikeableAndLikedFalse(Long liker, Long userLikeable);

    @Query("SELECT l1.userLikeable FROM LikeUserEntry l1 " +
            "WHERE l1.eventId = :event AND l1.isLiked = :like AND l1.liker = :user")
    List<Long> getLikes(Long user, Long event, boolean like);
}
