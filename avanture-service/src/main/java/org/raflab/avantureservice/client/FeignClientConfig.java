package org.raflab.avantureservice.client;

import feign.codec.ErrorDecoder;
import org.raflab.avantureservice.exceptions.PreservingFeignErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new PreservingFeignErrorDecoder();
    }
}
