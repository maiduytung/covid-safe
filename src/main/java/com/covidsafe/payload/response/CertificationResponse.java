package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
public class CertificationResponse implements Serializable {
    @JsonProperty("identification")
    private String identification;

    @JsonProperty("count_the_dose")
    private int countTheDose;

    @JsonProperty("last_modified_date")
    private Instant lastModifiedDate;
}
