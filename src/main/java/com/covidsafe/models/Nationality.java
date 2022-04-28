package com.covidsafe.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "nationalities")
@NoArgsConstructor
public class Nationality {

    @Id
    private String id;

    private String nationality;

}
