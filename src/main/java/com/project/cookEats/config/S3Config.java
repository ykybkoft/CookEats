package com.project.cookEats.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;

    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
