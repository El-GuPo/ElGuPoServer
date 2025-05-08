package com.elgupo.elguposerver.s3.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="user_photos")
public class UserPhoto {
    @Id
    @Column(name="id")
    private Long id;

    @Column(name="photo_url")
    private String photoUrl;

}
