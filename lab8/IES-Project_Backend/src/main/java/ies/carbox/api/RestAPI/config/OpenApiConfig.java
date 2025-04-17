package ies.carbox.api.RestAPI.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Carbox API", version = "v1", description = "API for carbox app"))
public class OpenApiConfig {
    // Configuration for OpenAPI goes here
}
