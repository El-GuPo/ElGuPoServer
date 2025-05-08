package com.elgupo.elguposerver.s3.repository;

import com.elgupo.elguposerver.s3.model.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPhotoRepository extends JpaRepository<UserPhoto, Long> {
}
