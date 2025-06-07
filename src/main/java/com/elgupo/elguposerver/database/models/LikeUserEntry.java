package com.elgupo.elguposerver.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes_users", schema = "public")
@Entity
public class LikeUserEntry {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "liker")
    private Long liker;

    @Column(name = "userLikeable")
    private Long userLikeable;

    @Column(name = "isLiked")
    private boolean isLiked;

    @Column(name = "eventId")
    private Long eventId;

    public LikeUserEntry(Long userLikeable, Long eventId, Long liker, boolean isLiked) {
        this.liker = liker;
        this.userLikeable = userLikeable;
        this.eventId = eventId;
        this.isLiked = isLiked;
    }
}
