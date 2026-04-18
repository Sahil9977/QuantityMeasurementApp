package com.app.quantitymeasurement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title       = "Quantity Measurement API",
        version     = "1.0",
        description = "REST API for quantity measurement operations — UC17 Spring Boot Migration",
        contact     = @Contact(name = "Quantity Measurement App")
    )
)
@Configuration
public class OpenApiConfig {
    // SpringDoc auto-configures the rest; this bean just provides metadata.
}
