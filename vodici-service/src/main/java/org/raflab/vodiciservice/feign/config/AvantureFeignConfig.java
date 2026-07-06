package org.raflab.vodiciservice.feign.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class AvantureFeignConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public ErrorDecoder avantureErrorDecoder() {
        return (methodKey, response) -> {
            HttpStatus status = HttpStatus.resolve(response.status());
            if (status == null) {
                status = HttpStatus.BAD_GATEWAY;
            }
            return new ResponseStatusException(status, extractMessage(methodKey, response, "Avanture servis"));
        };
    }

    private String extractMessage(String methodKey, Response response, String serviceName) {
        String rawBody = "";
        try {
            if (response.body() != null) {
                rawBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
        }

        if (!rawBody.isBlank()) {
            try {
                JsonNode root = objectMapper.readTree(rawBody);
                if (root.hasNonNull("message")) {
                    return root.get("message").asText();
                }
                if (root.hasNonNull("error")) {
                    return root.get("error").asText();
                }
            } catch (Exception ignored) {
                return rawBody;
            }
            return rawBody;
        }

        return serviceName + " call failed for " + methodKey + " with status " + response.status();
    }
}
