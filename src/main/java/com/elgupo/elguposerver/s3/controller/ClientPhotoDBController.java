package com.elgupo.elguposerver.s3.controller;

import com.elgupo.elguposerver.s3.model.UserPhoto;
import com.elgupo.elguposerver.s3.service.ClientPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientPhotoDBController {

    private final ClientPhotoService clientPhotoService;

    @Autowired
    public ClientPhotoDBController(ClientPhotoService clientPhotoService){
        this.clientPhotoService = clientPhotoService;
    }

    @PostMapping(value = "/clients")
    public ResponseEntity<?> create (@RequestBody UserPhoto userPhoto){
        clientPhotoService.create(userPhoto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping(value = "/clients")
    public ResponseEntity<List<UserPhoto>> read() {
        try {
            final List<UserPhoto> clients = clientPhotoService.readAll();

            return clients != null && !clients.isEmpty()
                    ? new ResponseEntity<>(clients, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            System.out.println("ERROR SQL!!");
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping(value = "/clients/{id}")
    public ResponseEntity<UserPhoto> read(@PathVariable(name = "id") Long id) {
        final UserPhoto userPhoto = clientPhotoService.read(id);

        return userPhoto != null
                ? new ResponseEntity<>(userPhoto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/clients/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") Long id, @RequestBody UserPhoto userPhoto) {
        final boolean updated = clientPhotoService.update(userPhoto, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/clients/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        final boolean deleted = clientPhotoService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }


}
