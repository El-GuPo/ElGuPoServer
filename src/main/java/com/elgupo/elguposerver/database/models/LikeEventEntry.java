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
@Table(name = "likes_events", schema = "public")
@Entity
public class LikeEventEntry {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "eventId")
    private Long eventId;

    @Column(name = "catId")
    private Long catId;

    public LikeEventEntry(final Long userId, final Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    public LikeEventEntry(final Long userId, final Long eventId, final Long catId) {
        this.userId = userId;
        this.eventId = eventId;
        this.catId = catId;
    }
}
