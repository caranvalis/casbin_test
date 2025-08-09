package com.casbin.casbin_test;

import com.casbin.casbin_test.application.services.AuthorizationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CasbinTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasbinTestApplication.class, args);
    }

    @Bean
    public CommandLineRunner initMetrics(AuthorizationService authorizationService) {
        return args -> {
            System.out.println("Initialisation des métriques...");
            authorizationService.initializePolicies();
            authorizationService.hasPermission("admin", "/api/users", "GET").block();
            authorizationService.hasPermission("user", "/api/users", "DELETE").block();
            System.out.println("Métriques générées avec succès");
        };
    }
}