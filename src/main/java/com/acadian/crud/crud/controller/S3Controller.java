package com.acadian.crud.crud.controller;

import com.acadian.crud.crud.entity.S3Request;
import com.acadian.crud.crud.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/s3")
public class S3Controller {
    private final S3Service s3Service;
    private static final Logger logger = LoggerFactory.getLogger(S3Controller.class);

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBucket(@RequestParam String name) {
        logger.info("Post/ Create Bucket Called");
        return new ResponseEntity<>(s3Service.createBucket(name), HttpStatus.CREATED);
    }

    @GetMapping("/buckets")
    public ResponseEntity<List<String>> listAllBuckets() {
        logger.info("Get/ List all buckets Called");
        return ResponseEntity.ok(s3Service.listAllBuckets());
    }

    @PostMapping("/upload")
    public ResponseEntity<S3Request> uploadFiles(@RequestBody S3Request s3Request){
        logger.info("Post/ Upload files Called");
        return ResponseEntity.ok(s3Service.uploadFiles(s3Request));
    }

    @GetMapping("/files")
    public ResponseEntity<List<S3Request>> listFiles(S3Request s3Request){
    logger.info("Get/ List files Called");
    return ResponseEntity.ok(s3Service.listFiles(s3Request));
    }



}
