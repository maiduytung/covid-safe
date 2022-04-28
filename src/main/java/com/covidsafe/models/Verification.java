package com.covidsafe.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@Document(collection = "verifications")
public class Verification {
    @Id
    private String id;

    @NotBlank
    private String verificationKey;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    @Indexed(name = "verificationExpireAtIndex", expireAfterSeconds = 3000000)
    private Date expireAt;

    public Verification(String id, String verificationKey) {
        this.id = id;
        this.verificationKey = verificationKey;
        this.expireAt = Date.from(Instant.now().plus(300, ChronoUnit.SECONDS));
    }
}
