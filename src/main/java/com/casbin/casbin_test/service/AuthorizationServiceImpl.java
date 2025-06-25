package com.casbin.casbin_test.service;

import com.casbin.casbin_test.service.AuthorizationService;
import org.casbin.jcasbin.main.Enforcer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl extends AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    private final Enforcer enforcer;

    @Autowired
    public AuthorizationServiceImpl(Enforcer enforcer) {
        this.enforcer = enforcer;
    }

    @Override
    public boolean hasPermission(String user, String resource, String action) {
        boolean result = enforcer.enforce(user, resource, action);
        logger.debug("Authorization check: user={}, resource={}, action={}, result={}",
                    user, resource, action, result);
        return result;
    }
}