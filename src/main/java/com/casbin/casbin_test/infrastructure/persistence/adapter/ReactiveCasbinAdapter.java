package com.casbin.casbin_test.infrastructure.persistence.adapter;

import org.casbin.jcasbin.model.Model;
import reactor.core.publisher.Mono;
import java.util.List;

public interface ReactiveCasbinAdapter {
    Mono<Void> loadPolicyAsync(Model model);
    Mono<Void> savePolicyAsync(Model model);
    Mono<Void> addPolicyAsync(String sec, String ptype, List<String> rule);
    Mono<Void> removePolicyAsync(String sec, String ptype, List<String> rule);
    Mono<Void> removeFilteredPolicyAsync(String sec, String ptype, int fieldIndex, String... fieldValues);
}
