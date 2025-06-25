package com.casbin.casbin_test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Casbin Test")
                        .version("1.0")
                        .description("Documentation de l'API Casbin avec Springdoc OpenAPI"));
    }
}
