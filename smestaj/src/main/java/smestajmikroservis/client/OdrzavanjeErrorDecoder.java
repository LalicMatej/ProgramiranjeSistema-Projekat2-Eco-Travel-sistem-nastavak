package smestajmikroservis.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.EntityNotFoundException;
import smestajmikroservis.exception.ServiceUnavailableException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class OdrzavanjeErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = readBody(response);
        return switch (response.status()) {
            case 400 -> new IllegalArgumentException(body);
            case 404 -> new EntityNotFoundException(body);
            case 409 -> new IllegalStateException(body);
            case 503 -> new ServiceUnavailableException(body);
            default  -> defaultDecoder.decode(methodKey, response);
        };
    }

    private String readBody(Response response) {
        if (response.body() == null) {
            return "Greška u odrzavanje servisu (HTTP " + response.status() + ")";
        }
        try (InputStream is = response.body().asInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Greška u odrzavanje servisu (HTTP " + response.status() + ")";
        }
    }
}
