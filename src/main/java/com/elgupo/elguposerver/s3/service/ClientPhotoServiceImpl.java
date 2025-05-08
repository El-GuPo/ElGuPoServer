package com.elgupo.elguposerver.s3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
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
    public URL getPhoto(Long userID) {
        /*
            Need validator, everybody can get photo
         */
        return createPresignedGetURL(getPath(userID));
    }

    @Override
    public URL uploadPhoto(Long userId, MultipartFile photo) throws IOException {
        /*
            Need validator user should be able to put photo
         */
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
    public boolean deletePhoto(Long userID) {
        /*
            Need validator, same validation as in put
         */

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET)
                .key(getPath(userID))
                .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (AwsServiceException e) {
            throw new ResponseStatusException(
                    HttpStatus.valueOf(e.statusCode()),
                    "S3 error: " + e.awsErrorDetails().errorMessage()
            );

        } catch (SdkClientException e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "S3 client error: " + e.getMessage()
            );
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
