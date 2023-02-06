package com.content_i_like.controller.restcontroller;

import com.content_i_like.domain.Response;
import com.content_i_like.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadRestController {

  private final S3FileUploadService s3FileUploadService;

  @PostMapping
  public Response<String> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
    String url = s3FileUploadService.uploadFile(file);
    return Response.success(url);
  }
}
