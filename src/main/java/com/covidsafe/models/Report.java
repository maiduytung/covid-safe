package com.covidsafe.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "reports")
@NoArgsConstructor
public class Report extends AuditMetadata {

    @Id
    private String id;

    @Field("user_id")
    private String userId;

    @Field("report")
    private String report;

    @DBRef
    private Subnational province;

    @DBRef
    private Subnational district;

    @DBRef
    private Subnational ward;

    private String address;

    private int status;
}
