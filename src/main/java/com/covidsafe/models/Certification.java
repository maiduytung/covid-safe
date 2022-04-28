package com.covidsafe.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "certifications")
@NoArgsConstructor
public class Certification extends AuditMetadata {

    @Id
    @Field("identification")
    private String identification;

    @Field("count_the_dose")
    private int countTheDose;
}
