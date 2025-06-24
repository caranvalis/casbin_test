package com.casbin.casbin_test.interceptor;

import com.casbin.casbin_test.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Récupérer l'utilisateur depuis le token JWT ou la session
        String currentUser = getCurrentUser(request);

        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // Vérifier l'autorisation
        if (!authorizationService.hasPermission(currentUser, uri, method)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        return true;
    }

    private String getCurrentUser(HttpServletRequest request) {
        // Implémentation pour récupérer l'utilisateur actuel
        // Par exemple depuis un token JWT ou une session
        return request.getHeader("X-User"); // Exemple simple
    }
}
