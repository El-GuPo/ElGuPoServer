package com.elgupo.elguposerver.database.repositories;

import com.elgupo.elguposerver.database.models.LikeEventEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeEventRepository extends CrudRepository<LikeEventEntry, Integer> {

    List<LikeEventEntry> findByUserId(Long userId);

    @Query("SELECT l1.eventId FROM LikeEventEntry l1 " +
            "INNER JOIN LikeEventEntry l2 ON l1.eventId = l2.eventId " +
            "WHERE l1.userId = :userId1 AND l2.userId = :userId2")
    List<Long> findCommonEventsByUsersId(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2
    );
}
