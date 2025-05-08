package com.elgupo.elguposerver.s3.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

public interface ClientPhotoService {

    URL getPhoto(Long id);

    URL uploadPhoto(Long userId, MultipartFile photo) throws IOException;

    boolean deletePhoto(Long userID);
}
