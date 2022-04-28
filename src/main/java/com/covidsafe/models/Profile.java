package com.covidsafe.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "profiles")
@NoArgsConstructor
public class Profile extends AuditMetadata {
    @Id
    private String id;

    private String userId;

    private Binary avatar;

    @Field("qr_code")
    private Binary qrCode;

    @Field("full_name")
    private String fullName;

    @Field("date_of_birth")
    private Date dateOfBirth;

    private boolean gender;

    @Field("phone_number")
    private String phoneNumber;

    private String identification;

    private String email;

    @DBRef
    private Subnational province;

    @DBRef
    private Subnational district;

    @DBRef
    private Subnational ward;

    private String address;

    @Field("health_insurance_number")
    private String healthInsuranceNumber;

    @DBRef
    private Nationality nationality;

    @DBRef
    private Ethnic ethnic;

    private String religion;

    private String occupation;

}
