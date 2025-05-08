package com.elgupo.elguposerver.s3.service;

import com.elgupo.elguposerver.s3.model.UserPhoto;
import com.elgupo.elguposerver.s3.repository.ClientPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientPhotoServiceImpl implements ClientPhotoService{

    @Autowired
    private ClientPhotoRepository userPhotoRepository;

    @Override
    public void create(UserPhoto userPhoto) {
        userPhotoRepository.save(userPhoto);
    }

    @Override
    public List<UserPhoto> readAll() {
        return userPhotoRepository.findAll();
    }

    @Override
    public UserPhoto read(Long id) {
        return userPhotoRepository.findById(id).orElse(null);
    }

    @Override
    public boolean update(UserPhoto userPhoto, Long id) {
        if(userPhotoRepository.existsById(id)){
            userPhoto.setId(id);
            userPhotoRepository.save(userPhoto);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        if(userPhotoRepository.existsById(id)){
            userPhotoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
