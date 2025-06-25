package com.casbin.casbin_test.interceptor;

import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class UserAuthenticationInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Autowired
    public UserAuthenticationInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("X-User-Id");
        if (userId != null && !userId.isEmpty()) {
            User user = userService.findById(userId);
            if (user != null) {
                request.setAttribute("currentUser", user);
                return true;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}