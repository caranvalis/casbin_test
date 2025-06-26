package com.casbin.casbin_test.interceptor;

import com.casbin.casbin_test.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthorizationInterceptor implements WebFilter, HandlerInterceptor {

    private final AuthorizationService authorizationService;

    public AuthorizationInterceptor(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
      ServerHttpRequest request = exchange.getRequest();
      String currentUser = getCurrentUser(request);

      if (currentUser == null) {
          exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
          return exchange.getResponse().setComplete();
      }

      String uri = request.getPath().value();
      String method = request.getMethod().name();  // Correction ici

      return authorizationService.hasPermission(currentUser, uri, method)
              .flatMap(hasPermission -> {
                  if (!hasPermission) {
                      exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                      return exchange.getResponse().setComplete();
                  }
                  return chain.filter(exchange);
              });
  }

    private String getCurrentUser(ServerHttpRequest request) {
        // Obtenir l'utilisateur depuis l'en-tête
        List<String> userHeaders = request.getHeaders().get("X-User");
        return userHeaders != null && !userHeaders.isEmpty() ? userHeaders.get(0) : null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}