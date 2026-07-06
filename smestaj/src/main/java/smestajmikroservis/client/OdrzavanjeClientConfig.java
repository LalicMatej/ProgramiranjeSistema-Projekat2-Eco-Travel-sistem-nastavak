package smestajmikroservis.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class OdrzavanjeClientConfig {

    @Bean
    public ErrorDecoder odrzavanjeErrorDecoder() {
        return new OdrzavanjeErrorDecoder();
    }
}
