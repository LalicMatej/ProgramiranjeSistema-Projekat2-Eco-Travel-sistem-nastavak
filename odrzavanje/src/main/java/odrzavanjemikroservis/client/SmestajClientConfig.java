package odrzavanjemikroservis.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class SmestajClientConfig {

    @Bean
    public ErrorDecoder smestajErrorDecoder() {
        return new SmestajErrorDecoder();
    }
}
