package com.companyname.projectname.scheduler;

import com.companyname.projectname.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DebitScheduler {

    private static Logger logger = LoggerFactory.getLogger(DebitScheduler.class);

    @Resource
    private S3Service s3Service;

    @Value("${aerospike.db.name}")
    private String databaseName;

    @Value("${s3.data.bucket}")
    String bucketName;


    //    @Scheduled(cron = "0 0 * * * ?")
//    @Scheduled(fixedDelay = 500000)
    public void job() {
    }




}
