package org.raflab.avantureservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AvantureServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvantureServiceApplication.class, args);
    }

}
