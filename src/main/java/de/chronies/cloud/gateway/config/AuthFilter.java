package de.chronies.cloud.gateway.config;

import de.chronies.cloud.gateway.dto.ApiExceptionDto;
import de.chronies.cloud.gateway.dto.AuthResponseDto;
import de.chronies.cloud.gateway.dto.GatewayAuthResponseDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return writeResponse(exchange.getResponse(), ApiExceptionDto.builder()
                        .message("Missing authentication information")
                        .status(HttpStatus.BAD_REQUEST)
                        .time(Instant.now())
                        .path(exchange.getRequest().getPath().toString())
                        .build()
                        .getJsonAsBytes());
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                return writeResponse(exchange.getResponse(), ApiExceptionDto.builder()
                        .message("Incorrect authentication structure")
                        .status(HttpStatus.BAD_REQUEST)
                        .time(Instant.now())
                        .path(exchange.getRequest().getPath().toString())
                        .build()
                        .getJsonAsBytes());
            }

            String body = "{\n" +
                          "\"token\":\"" + parts[1] + "\"" +
                          "}";

            return webClientBuilder.build()
                    .post()
                    .uri("http://USER-SERVICE/user/validateToken")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(body))
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().isError()) {
                            return clientResponse.bodyToMono(AuthResponseDto.class);
                        }
                        return clientResponse.bodyToMono(GatewayAuthResponseDto.class);
                    }).flatMap(dto -> {
                        if (dto.getClass() != GatewayAuthResponseDto.class) {
                            AuthResponseDto authResponseDto = (AuthResponseDto) dto;
                            return writeResponse(exchange.getResponse(), ApiExceptionDto.builder()
                                    .message(authResponseDto.getMessage())
                                    .status(authResponseDto.getStatus())
                                    .time(Instant.now())
                                    .path(authResponseDto.getPath())
                                    .build()
                                    .getJsonAsBytes());
                        }
                        GatewayAuthResponseDto gatewayAuthResponseDto = (GatewayAuthResponseDto) dto;
                        exchange.getRequest()
                                .mutate()
                                .header("x-auth-user-id", String.valueOf(gatewayAuthResponseDto.getUser_id()));
                        return chain.filter(exchange);
                    });


/*
            return webClientBuilder.build()
                    .post()
                    .uri("http://USER-SERVICE/user/validateToken?token=" + parts[1])
                    .retrieve()
                    .bodyToMono(UserDto.class)
                    .map(userDto -> {
                        exchange.getRequest()
                                .mutate()
                                .header("X-auth-user-id", String.valueOf(userDto.getId()));
                        return exchange;
                    }).flatMap(chain::filter);
*/

        };
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, byte[] bytes) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        response.getHeaders().add("Date", DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)
                .withLocale(Locale.US)
                .withZone(ZoneId.of("GMT"))
                .format(Instant.now()));

        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Flux.just(dataBuffer));
    }

    public static class Config {

    }

}
