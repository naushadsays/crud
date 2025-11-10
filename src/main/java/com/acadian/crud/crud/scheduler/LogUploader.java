package com.acadian.crud.crud.scheduler;

import com.acadian.crud.crud.entity.S3Request;
import com.acadian.crud.crud.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LogUploader {

    private static final Logger logger = LoggerFactory.getLogger(LogUploader.class);
    private final  S3Service s3Service;

    public LogUploader(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void uploadLogs() {
        String yesterdaysDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String logFilePath = String.format("logs/crud-application-%s.log", yesterdaysDate);
        S3Request s3Request = new S3Request();
        s3Request.setBucketName("employee-logs");
        s3Request.setFilePath(logFilePath);
        logger.info("Uploading logs to S3...");
        s3Service.uploadFiles(s3Request);
    }
}
