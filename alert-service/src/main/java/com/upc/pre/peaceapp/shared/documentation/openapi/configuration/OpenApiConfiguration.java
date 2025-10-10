package com.upc.pre.peaceapp.shared.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiConfiguration {

    @Value("${spring.application.name:PeaceApp}")
    private String applicationName;

    @Bean
    public OpenAPI peaceAppOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PeaceApp API")
                        .description("REST API documentation for the " + formatServiceName(applicationName) + " microservice in the PeaceApp system.")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Auto-generated documentation using SpringDoc OpenAPI 3.")
                        .url("https://springdoc.org/"))
                .servers(Collections.singletonList(
                        new Server().url("/").description("Local environment")));
    }

    private String formatServiceName(String name) {
        if (name == null || name.isEmpty()) return "Unknown Service";
        return name.replace("-", " ").replace("_", " ").trim().toUpperCase();
    }
}
