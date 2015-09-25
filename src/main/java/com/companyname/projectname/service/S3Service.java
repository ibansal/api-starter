package com.companyname.projectname.service;

import com.amazonaws.RequestClientOptions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class S3Service {

    private static Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Resource
    AmazonS3Client amazonS3Client;

    public boolean putFile(String bucket, String key, InputStream in) {
        try {
            PutObjectRequest putReq = new PutObjectRequest(bucket, key, in, new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicReadWrite);
            RequestClientOptions requestClientOptions = putReq.getRequestClientOptions();
            requestClientOptions.setReadLimit(10 * 1024 * 1024);
            amazonS3Client.putObject(putReq);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean putFile(String bucket, String key, InputStream in, String contentType) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(contentType);
            PutObjectRequest putReq = new PutObjectRequest(bucket, key, in, objectMetadata).withCannedAcl(CannedAccessControlList.PublicReadWrite);
            RequestClientOptions requestClientOptions = putReq.getRequestClientOptions();
            requestClientOptions.setReadLimit(10 * 1024 * 1024);
            amazonS3Client.putObject(putReq);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public void deleteFile(String bucket, String key) {
        try {
            amazonS3Client.deleteObject(bucket, key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public boolean copy(String srcBucket, String srcKey, String destBucket, String destKey) {
        try {
            CopyObjectRequest copyReq = new CopyObjectRequest(srcBucket, srcKey, destBucket, destKey);
            copyReq.setCannedAccessControlList(CannedAccessControlList.PublicRead);
            amazonS3Client.copyObject(copyReq);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public List<String> getFileList(String bucket, String prefix) {
        List<String> list = new ArrayList<>();
        try {
            ListObjectsRequest listReq = new ListObjectsRequest().withBucketName(bucket);
            if(StringUtils.isNotBlank(prefix)) {
                listReq = listReq.withPrefix(prefix);
            }
            ObjectListing listObjects = amazonS3Client.listObjects(listReq);
            while (listObjects != null) {
                List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
                if (objectSummaries == null || objectSummaries.size() == 0) {
                    break;
                }
                for (S3ObjectSummary summary : objectSummaries) {
                    list.add(summary.getKey());
                }
                listObjects = amazonS3Client.listNextBatchOfObjects(listObjects);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    public List<S3ObjectSummary> getFileListSummary(String bucket, String prefix) {
        List<S3ObjectSummary> list = new ArrayList<>();
        try {
            ListObjectsRequest listReq = new ListObjectsRequest().withBucketName(bucket);
            if(StringUtils.isNotBlank(prefix)) {
                listReq = listReq.withPrefix(prefix);
            }
            ObjectListing listObjects = amazonS3Client.listObjects(listReq);
            while (listObjects != null) {
                List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
                if (objectSummaries == null || objectSummaries.size() == 0) {
                    break;
                }
                for (S3ObjectSummary summary : objectSummaries) {
                    list.add(summary);
                }
                listObjects = amazonS3Client.listNextBatchOfObjects(listObjects);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    public String getLatestFileInBucket(String bucketName) {
        return getLatestFileInBucket(bucketName, "");
    }

    public String getLatestFileInBucket(String bucketName, String suffix) {
        List<S3ObjectSummary> s3ObjectSummaries = getFileListSummary(bucketName, suffix);
        if (CollectionUtils.isEmpty(s3ObjectSummaries)) {
            return null;
        }
        Collections.sort(s3ObjectSummaries, new Comparator<S3ObjectSummary>() {
            @Override
            public int compare(S3ObjectSummary o1, S3ObjectSummary o2) {
                return (o1.getLastModified().getTime() > o2.getLastModified().getTime() ? -1 : 1);
            }
        });
        return s3ObjectSummaries.get(0).getKey();
    }

    public InputStream getS3InputStream(String bucket, String latestKeyInBucket) {
        S3Object object = amazonS3Client.getObject(
                new GetObjectRequest(bucket, latestKeyInBucket));
        if(object != null) {
            return object.getObjectContent();
        }
        return null;
    }
}
