package de.chronies.cloud.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GatewayAuthResponseDto {

    private int user_id;
    private String user_email;
}
