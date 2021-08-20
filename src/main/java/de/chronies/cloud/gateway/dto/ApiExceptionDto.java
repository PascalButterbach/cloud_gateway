package de.chronies.cloud.gateway.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;

@Getter
public class ApiExceptionDto {

    private final String message;
    private final HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE, dd MMM yyyy HH:mm:ss z", timezone = "Europe/Berlin", locale = "de_DE")
    private final Instant time;
    private final String path;

    @JsonIgnore
    public ApiExceptionDto(String message, HttpStatus status, String path) {
        this.message = message;
        this.status = status;
        this.time = Instant.now();
        this.path = path;
    }

    @JsonIgnore
    public byte[] getJsonAsBytes(){
        ObjectMapper objectMapper = new ObjectMapper();

        String val = "";

        try {
            val = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            val = "ApiExceptionDto could not be parsed to Json";
        }

        return val.getBytes(StandardCharsets.UTF_8);
    }

}