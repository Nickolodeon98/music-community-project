package com.content_i_like.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3FileUploadService {

  private final AmazonS3 s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private String defaultUrl = "https://content-i-like.s3.ap-northeast-2.amazonaws.com/";

  public String uploadFile(MultipartFile file) throws IOException {
    String fileName = generateFileName(file);
    try {
      s3Client.putObject(bucketName, fileName, file.getInputStream(), getObjectMetadata(file));
      return defaultUrl + fileName;
    } catch (SdkClientException e) {
      throw new IOException("Error uploading file to S3", e);
    }
  }

  private ObjectMetadata getObjectMetadata(MultipartFile file) {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(file.getContentType());
    objectMetadata.setContentLength(file.getSize());
    return objectMetadata;
  }

  private String generateFileName(MultipartFile file) {
    return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
  }

  public void deleteFile(String fileName) throws IOException {
    try {
      s3Client.deleteObject(bucketName, fileName);
    } catch (SdkClientException e) {
      throw new IOException("Error deleting file from S3", e);
    }
  }
}