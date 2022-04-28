package com.covidsafe.config;

import com.covidsafe.models.*;
import org.bson.types.Binary;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        // Tạo object và cấu hình
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        Converter<Role, String> roleNameToRoleString = new AbstractConverter<>() {
            @Override
            protected String convert(Role role) {
                return role == null ? null : role.getName().name();
            }
        };
        modelMapper.addConverter(roleNameToRoleString);

        Converter<Binary, String> binaryToBase64 = new AbstractConverter<>() {
            @Override
            protected String convert(Binary binary) {
                return binary == null ? null : Base64.getEncoder().encodeToString(binary.getData());
            }
        };
        modelMapper.addConverter(binaryToBase64);

        Converter<Subnational, String> subnationalToString = new AbstractConverter<Subnational, String>() {
            @Override
            protected String convert(Subnational subnational) {
                return subnational == null ? null : subnational.getId();
            }
        };
        modelMapper.addConverter(subnationalToString);

        Converter<Nationality, String> nationalityToString = new AbstractConverter<Nationality, String>() {
            @Override
            protected String convert(Nationality nationality) {
                return nationality == null ? null : nationality.getId();
            }
        };
        modelMapper.addConverter(nationalityToString);

        Converter<Ethnic, String> ethnicToString = new AbstractConverter<Ethnic, String>() {
            @Override
            protected String convert(Ethnic ethnic) {
                return ethnic == null ? null : ethnic.getId();
            }
        };
        modelMapper.addConverter(ethnicToString);

        Converter<Priority, String> priorityToString = new AbstractConverter<Priority, String>() {
            @Override
            protected String convert(Priority priority) {
                return priority == null ? null : priority.getId();
            }
        };
        modelMapper.addConverter(priorityToString);

        return modelMapper;
    }
}