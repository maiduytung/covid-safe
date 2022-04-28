package com.covidsafe.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
public class AuditMetadata {

    @CreatedDate
    @Field("created_date")
    private Instant createdDate;

    @LastModifiedDate
    @Field("last_modified_date")
    private Instant lastModifiedDate;

    @CreatedBy
    @Field("created_by")
    private String createdBy;

    @LastModifiedBy
    @Field("last_modified_by")
    private String lastModifiedBy;
}