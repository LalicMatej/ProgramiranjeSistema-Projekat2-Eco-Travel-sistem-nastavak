package org.raflab.avantureservice.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PreservingFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        return new AdventureProcessingException(extractMessage(methodKey, response), status);
    }

    private String extractMessage(String methodKey, Response response) {
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

        return "Vodici servis call failed for " + methodKey + " with status " + response.status();
    }
}
