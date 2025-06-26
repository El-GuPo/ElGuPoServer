package com.elgupo.elguposerver.s3.service;

import com.elgupo.elguposerver.s3.exceptions.BadRequestException;
import com.elgupo.elguposerver.s3.exceptions.InternalServerException;
import com.elgupo.elguposerver.s3.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

@Service
public class ClientPhotoServiceImpl implements ClientPhotoService {

    private final String BUCKET;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public ClientPhotoServiceImpl(
            @Value("${S3.key_id}") String keyId,
            @Value("${S3.secret_key}") String secretKey,
            @Value("${S3.region}") String region,
            @Value("${S3.endpoint}") String s3Endpoint,
            @Value("${S3.bucket}") String bucket
    ) {
        BUCKET = bucket;

        AwsCredentials credentials = AwsBasicCredentials.create(keyId, secretKey);

        s3Client = S3Client.builder()
                .httpClient(ApacheHttpClient.create())
                .region(Region.of(region))
                .endpointOverride(URI.create(s3Endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(s3Endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Override
    public URL getPhoto(Long userID) throws NotFoundException {
        try {
            s3Client.headObject(builder -> builder.bucket(BUCKET).key(getPath(userID)));
            return createPresignedGetURL(getPath(userID));
        } catch (NoSuchKeyException e) {
            throw new NotFoundException("Photo not found for user: " + userID);
        }
    }

    @Override
    public URL uploadPhoto(Long userId, MultipartFile photo) throws IOException {
        validateFile(photo);
        String key = getPath(userId);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(key)
                .contentType(photo.getContentType())
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(photo.getBytes()));
        return createPresignedGetURL(key);
    }

    @Override
    public boolean deletePhoto(Long userID) throws InternalServerException {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(getPath(userID))
                .build();

        try {
            s3Client.headObject(builder -> builder.bucket(BUCKET).key(getPath(userID)));
            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            throw new InternalServerException("S3 error: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) throws BadRequestException {
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new BadRequestException("Invalid file");
        }
    }

    private String getPath(Long userID) {
        return "userPhotos/" + userID;
    }

    private URL createPresignedGetURL(String key) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(BUCKET)
                .key(key)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(objectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

        return presignedRequest.url();
    }
}
