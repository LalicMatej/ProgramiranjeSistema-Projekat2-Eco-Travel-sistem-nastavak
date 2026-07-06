package org.example.financeservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Finance Service API",
                version = "v1-zadatak1",
                description = "Verzija 1 za finansije",
                contact = @Contact(name = "Prog sistema team")
        ),
        servers = @Server(url = "http://localhost:8082", description = "Local environment")
)
public class OpenApiConfig {
}
