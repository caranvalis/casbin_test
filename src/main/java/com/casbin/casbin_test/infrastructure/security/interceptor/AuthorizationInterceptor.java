package com.casbin.casbin_test.infrastructure.security.interceptor;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Component
public class AuthorizationInterceptor implements WebFilter {

    @Autowired
    private Enforcer enforcer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();

        // Autoriser les URLs publiques
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources") || path.startsWith("/webjars")) {
            return chain.filter(exchange);
        }

        // Extraire l'utilisateur depuis l'authentification Basic
        String user = extractUserFromBasicAuth(exchange);
        if (user == null) {
            user = "public";
        }

        // Log pour débugger
        System.out.println("Tentative d'accès: " + user + " -> " + path + " -> " + method);

        boolean authorized = false;
        try {
            // Traitement spécial pour admin
            if ("admin".equals(user)) {
                authorized = true;
                System.out.println("Accès admin autorisé directement");
            } else if ("Zaim".equals(user)) {  // Également autorisé directement (selon les logs)
                authorized = true;
                System.out.println("Accès Zaim autorisé directement");
            } else {
                authorized = enforcer.enforce(user, path, method);
                System.out.println("Résultat enforce pour " + user + ": " + authorized);
            }
        } catch (Exception e) {
            System.err.println("Erreur Casbin: " + e.getMessage());
            e.printStackTrace();
        }

        if (!authorized) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private String extractUserFromBasicAuth(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authHeader.substring("Basic ".length());
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                return credentials.split(":", 2)[0]; // Extraire le nom d'utilisateur
            } catch (Exception e) {
                System.err.println("Erreur lors du décodage des identifiants Basic: " + e.getMessage());
            }
        }
        return null;
    }
}