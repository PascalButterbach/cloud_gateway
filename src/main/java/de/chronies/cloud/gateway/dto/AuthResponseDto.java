package de.chronies.cloud.gateway.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthResponseDto {

    private String message;
    private HttpStatus status;
    private String time;
    private String path;

}
