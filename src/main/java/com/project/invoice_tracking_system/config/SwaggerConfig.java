package com.project.invoice_tracking_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define a security scheme named "bearerAuth" to use JWTs
        SecurityScheme bearerAuthScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");

        return new OpenAPI()
            .info(new Info().title("Invoice Tracking API").version("1.0"))
            .components(new Components().addSecuritySchemes("bearerAuth", bearerAuthScheme))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
