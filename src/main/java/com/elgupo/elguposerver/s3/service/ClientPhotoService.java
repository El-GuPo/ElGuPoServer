package com.elgupo.elguposerver.s3.service;

import com.elgupo.elguposerver.s3.model.UserPhoto;

import java.util.List;

public interface ClientPhotoService {

    void create(UserPhoto userPhoto);

    List<UserPhoto> readAll();

    UserPhoto read(Long id);

    boolean update(UserPhoto userPhoto, Long id);

    boolean delete(Long id);
}
