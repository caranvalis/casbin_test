package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.service.CasbinService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/casbin")
public class CasbinController {

    private final CasbinService casbinService;

    public CasbinController(CasbinService casbinService) {
        this.casbinService = casbinService;
    }

    @PostMapping("/policy/add")
    public Mono<Boolean> addPolicy(@RequestBody Map<String, Object> request) {
        List<String> params = (List<String>) request.get("params");
        return casbinService.addPolicy(params);
    }

    @GetMapping("/policies")
    public Mono<List<List<String>>> getPolicies() {
        return casbinService.getPolicies();
    }

    @PostMapping("/enforce")
    public Mono<Boolean> enforce(@RequestBody Map<String, String> request) {
        String sub = request.get("sub");
        String obj = request.get("obj");
        String act = request.get("act");
        return casbinService.checkPermission(sub, obj, act);
    }

    @PostMapping("/policy/delete")
    public Mono<Boolean> deletePolicy(@RequestBody Map<String, Object> request) {
        List<String> params = (List<String>) request.get("params");
        return casbinService.removePolicy(params);
    }

    @PostMapping("/policy/clear")
    public Mono<Void> clearPolicy() {
        return casbinService.clearPolicy();
    }

    @GetMapping("/debug")
    public Mono<Map<String, Object>> debugCasbin() {
        return casbinService.getPolicies()
                .map(policies -> Map.of("policies", policies));
    }
}