package com.casbin.casbin_test.interceptor;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationInterceptor implements WebFilter {

    @Autowired
    private Enforcer enforcer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();

        // Autoriser Swagger, docs et ressources statiques sans authentification Casbin
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")) {
            return chain.filter(exchange);
        }

        String user = exchange.getRequest().getHeaders().getFirst("X-User");

        if (user == null) {
            user = "default_user"; // utilisateur anonyme par défaut
        }

        boolean authorized = false;

        try {
            authorized = enforcer.enforce(user, path, method);
        } catch (Exception e) {
            System.err.println("Erreur Casbin : " + e.getMessage());
        }

        if (!authorized) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
