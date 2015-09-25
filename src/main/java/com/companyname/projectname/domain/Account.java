package com.companyname.projectname.domain;

import org.json.simple.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by ishan.bansal on 6/29/15.
 */
@Entity(name = "account")
@Table(name = "account",
        indexes = {@Index(name = "index_userId", columnList = "userId", unique = true),
                @Index(name = "index_advertiserId", columnList = "advertiserId", unique = true),
                @Index(name = "index_dateCreated", columnList = "dateCreated", unique = false)
        })
public class Account extends AbstractEntity {

    @Column(nullable = false)
    private BigDecimal outstanding;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, unique = true)
    private String advertiserId;

    @Column(nullable = false)
    private String name;


    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("outstanding", outstanding);
        jsonObject.put("userId", userId);
        jsonObject.put("advertiserId", advertiserId);
        jsonObject.put("active", getActive());
        jsonObject.put("dateCreated", getDateCreated());
        jsonObject.put("lastModified", getLastModified());
        jsonObject.put("name", getName());
        jsonObject.put("lastModified", getLastModified());
        jsonObject.put("id", getId());
        return jsonObject.toJSONString();
    }

    @Override
    public String toString() {
        return toJsonString().toString();
    }
}
