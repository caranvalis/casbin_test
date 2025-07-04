package com.casbin.casbin_test.controller;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/casbin")
public class CasbinController {

    private final Enforcer rbacEnforcer;

    // Gars on va  Garder uniquement ce constructeur
    public CasbinController(Enforcer rbacEnforcer) {
        this.rbacEnforcer = rbacEnforcer;
    }

    @PostMapping("/policy/add")
    public boolean addPolicy(@RequestBody Map<String, Object> request) {
        List<String> params = (List<String>) request.get("params");
        String[] stringArray = params.toArray(new String[0]);
        return rbacEnforcer.addPolicy(stringArray);
    }

    @GetMapping("/policies")
    public List<List<String>> getPolicies() {
        return rbacEnforcer.getPolicy();
    }

    @PostMapping("/enforce")
    public boolean enforce(@RequestBody Map<String, String> request) {
        String sub = request.get("sub");
        String obj = request.get("obj");
        String act = request.get("act");
        return rbacEnforcer.enforce(sub, obj, act);
    }

    @PostMapping("/policy/delete")
    public boolean deletePolicy(@RequestBody Map<String, Object> request) {
        List<String> params = (List<String>) request.get("params");
        String[] stringArray = params.toArray(new String[0]);
        return rbacEnforcer.removePolicy(stringArray);
    }

    @PostMapping("/policy/clear")
    public void clearPolicy() {
        rbacEnforcer.clearPolicy();
    }
}