package com.back.routopia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;

@Service
public class S3Service {

    private final S3Client s3;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public S3Service(@Value("${aws.accessKey}") String accessKey,
                     @Value("${aws.secretKey}") String secretKey,
                     @Value("${aws.region}") String region) {
        s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public String uploadFile(MultipartFile file){
        String key = System.currentTimeMillis() + "-" + sanitizeFileName(file.getOriginalFilename());
        return uploadFile(file, key);
    }

    /**
     * Reemplaza espacios, paréntesis y demás caracteres no alfanuméricos del nombre
     * original por "_" antes de usarlo como parte de la key de S3. Evita que la URL
     * resultante rompa el parsing de CSS url(...) sin comillas (espacio = fin de URL)
     * u otros contextos que no encodeen la URL automáticamente.
     */
    public static String sanitizeFileName(String filename) {
        if (filename == null || filename.isBlank()) {
            return "file";
        }
        return filename.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public String uploadFile(MultipartFile file, String key) {

        try {
            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            // Devuelves la URL del archivo
            return "https://" + bucketName + ".s3.amazonaws.com/" + key;

        } catch (Exception e) {
            throw new RuntimeException("Error al subir el archivo a S3", e);
        }
    }
}

