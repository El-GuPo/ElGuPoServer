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
@Table(name = "likes", schema = "public")
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

    public LikeEventEntry(final Long userId, final Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}
