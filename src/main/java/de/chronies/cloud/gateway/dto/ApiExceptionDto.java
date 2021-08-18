package de.chronies.cloud.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Builder
@Getter
@RequiredArgsConstructor
public class ApiExceptionDto {
    private final String message;
    private final HttpStatus status;
    private final Instant time;
    private final String path;

    public String getTime() {
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                .withLocale(Locale.US)
                .withZone(ZoneId.of("GMT"))
                .format(time);
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