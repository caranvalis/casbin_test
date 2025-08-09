package com.casbin.casbin_test.infrastructure.security.interceptor;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CasbinAuthorizationInterceptor implements WebFilter {

    private final Enforcer enforcer;

    public CasbinAuthorizationInterceptor(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String method = request.getMethod().name();

        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication())
                .filter(auth -> auth != null && auth.isAuthenticated())
                .map(Authentication::getName)
                .flatMap(username -> {
                    if (enforcer.enforce(username, path, method)) {
                        return chain.filter(exchange);
                    }
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}