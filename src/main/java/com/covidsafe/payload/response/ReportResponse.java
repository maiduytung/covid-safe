package com.covidsafe.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.Instant;

@Data
public class ReportResponse implements Serializable {
    private String id;

    @JsonProperty("report")
    @NotBlank
    private String report;

    @NotBlank
    private String province;

    @NotBlank
    private String district;

    @NotBlank
    private String ward;

    private String address;

    @JsonProperty("created_date")
    private Instant createdDate;

    private int status;
}
