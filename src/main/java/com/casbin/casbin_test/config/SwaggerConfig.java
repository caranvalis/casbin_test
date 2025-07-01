package com.casbin.casbin_test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin-api")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Casbin Test API")
                        .version("1.0.0")
                        .description("API de gestion des utilisateurs et d'autorisation avec Casbin")
                        .contact(new Contact()
                                .name("Équipe de développement")
                                .email("dev@example.com")
                        )
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}