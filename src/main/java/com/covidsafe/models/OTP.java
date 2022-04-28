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
@Document(collection = "otps")
public class OTP {
    @Id
    private String id;

    @NotBlank
    private String otp;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    @Indexed(name = "otpExpireAtIndex", expireAfterSeconds = 120)
    private Date expireAt;

    public OTP(String id, String otp) {
        this.id = id;
        this.otp = otp;
        this.expireAt = Date.from(Instant.now().plus(120, ChronoUnit.SECONDS));
    }
}
