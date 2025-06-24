package com.casbin.casbin_test.service;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationService {

    @Autowired
    private Enforcer enforcer;

    /**
     * Vérifie si un utilisateur a l'autorisation d'effectuer une action
     */
    public boolean hasPermission(String user, String resource, String action) {
        return enforcer.enforce(user, resource, action);
    }

    /**
     * Ajoute une politique
     */
    public boolean addPolicy(String user, String resource, String action) {
        return enforcer.addPolicy(user, resource, action);
    }

    /**
     * Supprime une politique
     */
    public boolean removePolicy(String user, String resource, String action) {
        return enforcer.removePolicy(user, resource, action);
    }

    /**
     * Ajoute un rôle à un utilisateur
     */
    public boolean addRoleForUser(String user, String role) {
        return enforcer.addRoleForUser(user, role);
    }

    /**
     * Supprime un rôle d'un utilisateur
     */
    public boolean deleteRoleForUser(String user, String role) {
        return enforcer.deleteRoleForUser(user, role);
    }

    /**
     * Récupère tous les rôles d'un utilisateur
     */
    public List<String> getRolesForUser(String user) {
        return enforcer.getRolesForUser(user);
    }

    /**
     * Retourne la liste des utilisateurs pour un rôle donné
     */
    public List<String> getUsersForRole(String role) {
        return enforcer.getUsersForRole(role);
    }
}
