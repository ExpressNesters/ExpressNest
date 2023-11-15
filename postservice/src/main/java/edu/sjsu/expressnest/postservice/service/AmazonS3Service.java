package edu.sjsu.expressnest.postservice.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class AmazonS3Service {

    @Autowired
    private AmazonS3 amazonS3Client;

    private String bucketName = "express-nest-bucket";

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), new ObjectMetadata());
        return fileName;
    }
}
