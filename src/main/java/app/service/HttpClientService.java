package app.service;

import app.data.exceptions.HttpException;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
public class HttpClientService {

    private static final int CONNECTION_TIMEOUT_S = 3000;
    private static final int READ_TIMEOUT_S = 5;
    private static final int FIVE_MB = 5000 * 1024;
    private static final int MINIMUM_LOG_LENGTH = 10000;


    private static WebClient getInstance() {
        return WebClient.builder()
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(codecs -> codecs
                                        .defaultCodecs()
                                        .maxInMemorySize(FIVE_MB))
                                .build())
                .clientConnector(
                        new ReactorClientHttpConnector(HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_S)
                                .responseTimeout(Duration.ofSeconds(READ_TIMEOUT_S)))
                ).build();

    }

    public String doGet(String url) {
        WebClient.RequestHeadersSpec<?> request = getInstance()
                .get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return executeRequest(request);
    }

    public String executeRequest(WebClient.RequestHeadersSpec<?> request) {
        val id = UUID.randomUUID();
        try {

            Mono<String> monoResponse = request.exchangeToMono(response -> {
                log.info("Received response [{}]: {}", id, response);
                if (response.statusCode().equals(HttpStatus.OK)) {
                    return response.bodyToMono(String.class);
                } else if (response.statusCode().is4xxClientError()) {
                    return Mono.just("Error response");
                } else {
                    return response.createException().flatMap(Mono::error);
                }
            });

            log.info("Executing request [{}]: {}.", id, request);
            String body = monoResponse
                    .doOnError(error -> {
                        throw new HttpException(String.format("An error has occurred %s", error.getMessage()));
                    }).block();

            if (body == null) {
                throw new HttpException("Empty response body " + id);
            }

            log.info("Extracted response body [{}]: {}", id,
                    body.length() > MINIMUM_LOG_LENGTH
                            ? "body size is more than " + MINIMUM_LOG_LENGTH
                            : body);

            return body;
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + request + " " + id, e);
        }
    }

}
