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

            final String path = exchange.getRequest().getPath().toString();

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                ApiExceptionDto apiException = new ApiExceptionDto("Missing authentication information", HttpStatus.BAD_REQUEST, path);
                return writeResponse(exchange.getResponse(), apiException.getJsonAsBytes());
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            //todo intellij acting up (.split) : impossible to be null... refactor
            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equalsIgnoreCase(parts[0])) {
                ApiExceptionDto apiException = new ApiExceptionDto("Incorrect authentication structure", HttpStatus.BAD_REQUEST, path);
                return writeResponse(exchange.getResponse(), apiException.getJsonAsBytes());
            }

            return webClientBuilder.build()
                    .post()
                    .uri("http://USER-SERVICE/token/validateToken")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().isError()) {
                            return clientResponse.bodyToMono(AuthResponseDto.class);
                        }
                        return clientResponse.bodyToMono(GatewayAuthResponseDto.class);
                    }).flatMap(dto -> {
                        if (dto.getClass() != GatewayAuthResponseDto.class) {
                            AuthResponseDto authResponseDto = (AuthResponseDto) dto;
                            ApiExceptionDto apiException = new ApiExceptionDto(authResponseDto.getMessage(), authResponseDto.getStatus(), authResponseDto.getPath());

                            return writeResponse(exchange.getResponse(), apiException.getJsonAsBytes());
                        }

                        GatewayAuthResponseDto gatewayAuthResponseDto = (GatewayAuthResponseDto) dto;
                        exchange.getRequest()
                                .mutate()
                                .headers(headers -> {
                                    headers.set("x-auth-user-id", String.valueOf(gatewayAuthResponseDto.getUser_id()));
                                    headers.set("x-auth-user-email", gatewayAuthResponseDto.getUser_email());
                                });
                        return chain.filter(exchange);
                    });
        };
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, byte[] bytes) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        response.getHeaders().add(HttpHeaders.DATE, DateTimeFormatter.ofPattern("EEE dd MMM yyyy HH:mm:ss z", Locale.GERMANY)
                .withLocale(Locale.GERMANY)
                .withZone(ZoneId.of("CEST"))
                .format(Instant.now()));

        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Flux.just(dataBuffer));
    }

    public static class Config {

    }

}
