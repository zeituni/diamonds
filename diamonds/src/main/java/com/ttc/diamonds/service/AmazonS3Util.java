package com.ttc.diamonds.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


@Component
public class AmazonS3Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Util.class);

    /**
     * Helper method to upload object to S3. Caller can pass networkId to make it as S3 folder.
     *  @param localTempFile
     * @param size
     * @param s3FileName
     * @param s3ContentType
     * @param s3AccessKey
     * @param s3SecretKey
     * @param s3Region
     * @param s3Folder
     */
    public String uploadObject(InputStream localTempFile, long fileSize, String s3FileName, String s3ContentType, String s3AccessKey, String s3SecretKey, String s3Region, String s3Folder) throws FileNotFoundException {

        AmazonS3 amazonS3Client = getAmazonS3Client(s3AccessKey, s3SecretKey, s3Region);

        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(s3ContentType);
            objectMetadata.setContentLength(fileSize);

            String key = StringUtils.isNotBlank(s3Folder) ? s3Folder + "/" + s3FileName : s3FileName;

            PutObjectRequest putObjectRequest = new PutObjectRequest("ttc-diamonds", key, localTempFile, objectMetadata);

            amazonS3Client.putObject(putObjectRequest);

            LOGGER.info(String.format("video %s has been uploaded to AWS Bucket: %s ", s3FileName, "ttc-diamonds" + "/" + key));

            return "ttc-diamonds/" + key;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        } finally {
            IOUtils.closeQuietly(localTempFile);
        }
    }

    public URL generatePresignedUrl(String fileName, Long contextAccountId, Long userId, Date expiration, String contentType,
                                    String s3folder, String s3AccessKey, String s3SecretKey, String s3Region) {
        String key;

        if(userId == null) {
            key = StringUtils.isNotBlank(s3folder) ? s3folder + "/" + contextAccountId + "/" + fileName : contextAccountId + "/" + fileName;
        } else {
            key = StringUtils.isNotBlank(s3folder) ? s3folder + "/" + fileName : fileName;
        }


        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest("ttc", key);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);
        generatePresignedUrlRequest.setContentType(contentType);

        AmazonS3 amazonS3Client = getAmazonS3Client(s3AccessKey, s3SecretKey, s3Region);
        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    private AmazonS3 getAmazonS3Client(String awsAccessKey, String awsSecretKey, String awsRegion) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard().withCredentials(credentialsProvider).withRegion(Regions.US_EAST_1).build();
        return amazonS3Client;
    }

}
