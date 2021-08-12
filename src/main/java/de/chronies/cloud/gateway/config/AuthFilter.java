package de.chronies.cloud.gateway.config;

import de.chronies.cloud.gateway.dto.UserDto;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


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

            //TEST
            var buffer = exchange.getResponse().bufferFactory();


            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                //TEST
                return exchange.getResponse().writeWith(Flux.just(buffer.wrap("Missing authentication information".getBytes())));
                //throw new RuntimeException("Missing authentication information");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                //TEST
                return exchange.getResponse().writeWith(Flux.just(buffer.wrap("Incorrect authentication structure".getBytes())));
                //throw new RuntimeException("Incorrect authentication structure");
            }

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
        };
    }

    public static class Config {

    }

}
