package com.casbin.casbin_test.infrastructure.persistence.adapter;

import com.casbin.casbin_test.domain.model.CasbinRule;
import com.casbin.casbin_test.application.ports.ouput.CasbinRuleRepository;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.Helper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Component
public class R2dbcCasbinAdapter implements Adapter, ReactiveCasbinAdapter {

    private final CasbinRuleRepository repository;

    public R2dbcCasbinAdapter(CasbinRuleRepository repository) {
        this.repository = repository;
    }


    @Override
    public void loadPolicy(Model model) {
        loadPolicyAsync(model)
            .subscribeOn(Schedulers.boundedElastic())
            .block();
    }

    @Override
    public void savePolicy(Model model) {
        savePolicyAsync(model)
            .subscribeOn(Schedulers.boundedElastic())
            .block();
    }

    @Override
    public void addPolicy(String sec, String ptype, List<String> rule) {
        addPolicyAsync(sec, ptype, rule)
            .subscribeOn(Schedulers.boundedElastic())
            .block();
    }

    @Override
    public void removePolicy(String sec, String ptype, List<String> rule) {
        removePolicyAsync(sec, ptype, rule)
            .subscribeOn(Schedulers.boundedElastic())
            .block();
    }

    @Override
    public void removeFilteredPolicy(String sec, String ptype, int fieldIndex, String... fieldValues) {
        removeFilteredPolicyAsync(sec, ptype, fieldIndex, fieldValues)
            .subscribeOn(Schedulers.boundedElastic())
            .block();
    }

    // Implémentation de l'interface réactive ReactiveCasbinAdapter

    @Override
    public Mono<Void> loadPolicyAsync(Model model) {
        return repository.findAll()
                .doOnNext(rule -> {
                    String line = rule.getPtype();
                    for (int i = 0; i <= 5; i++) {
                        String val = rule.getField(i);
                        if (val != null && !val.isEmpty()) {
                            line += "," + val;
                        }
                    }
                    Helper.loadPolicyLine(line, model);
                })
                .then();
    }

    @Override
    public Mono<Void> savePolicyAsync(Model model) {
        return repository.deleteAll()
                .thenMany(
                        Flux.fromIterable(model.model.get("p").get("p").policy)
                                .map(rule -> {
                                    CasbinRule entity = new CasbinRule();
                                    entity.setPtype("p");
                                    for (int i = 0; i < rule.size(); i++) {
                                        entity.setField(i, rule.get(i));
                                    }
                                    return entity;
                                })
                                .concatWith(
                                        Flux.fromIterable(model.model.get("g").get("g").policy)
                                                .map(rule -> {
                                                    CasbinRule entity = new CasbinRule();
                                                    entity.setPtype("g");
                                                    for (int i = 0; i < rule.size(); i++) {
                                                        entity.setField(i, rule.get(i));
                                                    }
                                                    return entity;
                                                })
                                )
                )
                .flatMap(repository::save)
                .then();
    }

    @Override
    public Mono<Void> addPolicyAsync(String sec, String ptype, List<String> rule) {
        CasbinRule casbinRule = new CasbinRule();
        casbinRule.setPtype(ptype);
        for (int i = 0; i < rule.size(); i++) {
            casbinRule.setField(i, rule.get(i));
        }
        return repository.save(casbinRule).then();
    }

    @Override
    public Mono<Void> removePolicyAsync(String sec, String ptype, List<String> rule) {
        return repository.deleteByPtypeAndFields(ptype, rule);
    }

    @Override
    public Mono<Void> removeFilteredPolicyAsync(String sec, String ptype, int fieldIndex, String... fieldValues) {
        return repository.deleteByPtypeAndFieldIndex(ptype, fieldIndex, fieldValues);
    }
}