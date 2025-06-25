package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.User;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationService {

    @Autowired
    private Enforcer enforcer;

    @Autowired
    @Qualifier("abacEnforcer")
    private Enforcer abacEnforcer;

    public boolean hasPermission(String user, String resource, String action) {
        return enforcer.enforce(user, resource, action);
    }

    public boolean addPolicy(String user, String resource, String action) {
        return enforcer.addPolicy(user, resource, action);
    }

    public boolean removePolicy(String user, String resource, String action) {
        return enforcer.removePolicy(user, resource, action);
    }

    public boolean addRoleForUser(String user, String role) {
        return enforcer.addRoleForUser(user, role);
    }


    public boolean deleteRoleForUser(String user, String role) {
        return enforcer.deleteRoleForUser(user, role);
    }


    public List<String> getRolesForUser(String user) {
        return enforcer.getRolesForUser(user);
    }


    public List<String> getUsersForRole(String role) {
        return enforcer.getUsersForRole(role);
    }


    public boolean hasPermissionAbac(User user, String resource, String action) {
        return abacEnforcer.enforce(user, resource, action);
    }

}
