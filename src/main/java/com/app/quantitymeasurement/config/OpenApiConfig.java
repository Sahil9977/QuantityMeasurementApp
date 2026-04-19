package com.app.quantitymeasurement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * UC18 Swagger / OpenAPI configuration.
 *
 * @SecurityScheme adds a "bearerAuth" scheme to the Swagger UI.
 * After logging in via POST /api/v1/auth/login, copy the accessToken value,
 * click the "Authorize" button at the top of the Swagger UI page,
 * paste the token, and click "Authorize".
 * All endpoints marked with @SecurityRequirement("bearerAuth") will then
 * automatically include the "Authorization: Bearer <token>" header in
 * every "Try it out" request.
 */
@OpenAPIDefinition(
    info = @Info(
        title       = "Quantity Measurement API — UC18",
        version     = "2.0",
        description = "REST API with JWT authentication and Google OAuth2 login",
        contact     = @Contact(name = "Quantity Measurement App")
    )
)
@SecurityScheme(
    name        = "bearerAuth",
    type        = SecuritySchemeType.HTTP,
    scheme      = "bearer",
    bearerFormat = "JWT",
    description = "Paste your JWT token from POST /api/v1/auth/login here"
)
@Configuration
public class OpenApiConfig {
    // SpringDoc reads the annotations above and auto-generates the OpenAPI spec.
}
