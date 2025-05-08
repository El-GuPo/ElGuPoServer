package com.elgupo.elguposerver.s3.controller;

import com.elgupo.elguposerver.s3.service.ClientPhotoServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@Tag(name = "Photos")
@RequiredArgsConstructor
@ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
public class PhotoController {

    private final ClientPhotoServiceImpl clientPhotoService;

    @PutMapping(path = "/photos/{userID}", consumes = MULTIPART_FORM_DATA_VALUE)
    public URL uploadPhoto(@PathVariable("userID") Long userID, @RequestParam MultipartFile photo) throws IOException {
        return clientPhotoService.uploadPhoto(userID, photo);
    }

    @GetMapping("/photos/{userID}")
    public URL getPhoto(@PathVariable("userID") Long userID) {
        return clientPhotoService.getPhoto(userID);
    }

    @DeleteMapping("/photos/{userID}")
    public boolean deletePhoto(@PathVariable("userID") Long userID) throws IOException {
        return clientPhotoService.deletePhoto(userID);
    }

}
