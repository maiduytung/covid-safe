package com.covidsafe.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "subnationals")
@NoArgsConstructor
public class Subnational {

    @Id
    private String id;

    private String name;

    private String type;

    @Field("parent_id")
    private String parentId;

}
