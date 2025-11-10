package com.acadian.crud.crud.service;

import com.acadian.crud.crud.entity.S3Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final S3Client s3Client;
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);


    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public String createBucket(String bucketName) {
        try{
            if(s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build())!=null){
                logger.info("Bucket with name {} already exists",bucketName);
                return "Bucket already exists with name: " +bucketName;
            }
        }
        catch (Exception exception){
            logger.info("Bucket with name {} does not exist, creating new bucket",bucketName);
        }
        logger.info("Creating Bucket with name {}",bucketName);
        s3Client.createBucket(b -> b.bucket(bucketName));
        logger.info("Bucket Created Successfully {}",bucketName);
        return "Bucket created: " +bucketName;
    }

    public List<String> listAllBuckets() {
        logger.info("Listing all the buckets");
        ListBucketsResponse listBucketsResponse= s3Client.listBuckets();
        return  listBucketsResponse.buckets().stream().map(Bucket::name).collect(Collectors.toList());
    }

    public S3Request uploadFiles(S3Request s3Request){
        try {
            logger.info("Locating file with path {}",s3Request.getFilePath());
            File file = new File(s3Request.getFilePath());
            s3Request.setFileName(file.getName());
            logger.info("File located successfully, path {}",s3Request.getFilePath());
            logger.info("Saving file in S3 with name {}",s3Request.getFileName());
            s3Client.putObject(
                    PutObjectRequest
                            .builder()
                            .bucket(s3Request.getBucketName())
                            .key(s3Request.getFileName())
                            .build(), RequestBody.fromFile(file)
            );
            logger.info("Files successfully saved");
            s3Request.setStatus("Success");
            return s3Request;
        }
        catch (Exception exception){
            logger.error("Exception in S3Service class, issue in uploadFiles()");
        }
        s3Request.setStatus("Failed");
        return s3Request;
    }

    public List<S3Request> listFiles(S3Request s3Request){
        List<S3Request> list;
        try{
            logger.info("Listing files in bucket {}",s3Request.getBucketName());
            ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                    .bucket(s3Request.getBucketName()).build());
            list= listObjectsV2Response.contents().stream().map(s3Object -> {
                S3Request request = new S3Request();
                request.setBucketName(s3Request.getBucketName());
                request.setFileName(s3Object.key());
                request.setStatus("Found");
                return request;
            }).collect(Collectors.toList());
            return list;
        }
        catch (Exception exception){
            logger.error("Exception in S3Service class, issue in listFiles()");
        }
        return List.of(new S3Request ("Invalid", null, null, "Failed") );
    }

}
