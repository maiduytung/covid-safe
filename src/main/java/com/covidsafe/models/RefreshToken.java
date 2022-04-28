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
@Document(collection = "refreshes")
public class RefreshToken {
    @Id
    private String id;

    @NotBlank
    private String refreshToken;

    @NotBlank
    private String userId;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    @Indexed(name = "verificationExpireAtIndex", expireAfterSeconds = 2592000) // 90days
    private Date expireAt;

    public RefreshToken(String id, String userId, String refreshToken) {
        this.id = id;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expireAt = Date.from(Instant.now().plus(30, ChronoUnit.DAYS));
    }
}
