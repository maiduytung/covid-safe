package com.covidsafe.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ethnics")
@NoArgsConstructor
public class Ethnic {
    @Id
    private String id;

    private String name;
}
