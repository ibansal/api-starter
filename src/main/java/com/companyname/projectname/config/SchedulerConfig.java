package com.companyname.projectname.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableScheduling
@Configuration
public class SchedulerConfig {

    @Value("${debit.scheduler.threadpool.size}")
    private int threadPoolSize;

    @Value("${s3.accesskeyid}")
    private String accessKey;

    @Value("${s3.secretkey}")
    private String accessKeySecret;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(threadPoolSize);
    }

    @Bean
    public AmazonS3Client amazonS3Client() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSocketBufferSizeHints(10000000, 10000000);
        return new AmazonS3Client(new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return accessKey;
            }

            @Override
            public String getAWSSecretKey() {
                return accessKeySecret;
            }
        }, clientConfiguration);
    }
}
