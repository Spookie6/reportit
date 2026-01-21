package dev.reportit.networking.responses;

import com.fasterxml.jackson.core.JsonProcessingException;

import static dev.reportit.networking.AuthService.mapper;

public interface Response {

    default String toJson() {
        try {
            return mapper.writeValueAsString(this.getClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
