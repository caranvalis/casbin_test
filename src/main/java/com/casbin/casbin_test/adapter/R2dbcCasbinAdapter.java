package com.casbin.casbin_test.adapter;

import com.casbin.casbin_test.model.CasbinRule;
import com.casbin.casbin_test.repository.CasbinRuleRepository;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.Helper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class R2dbcCasbinAdapter implements Adapter {

    private final CasbinRuleRepository repository;

    public R2dbcCasbinAdapter(CasbinRuleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadPolicy(Model model) {
        repository.findAll().toStream().forEach(rule -> {
            String line = rule.getPtype();
            for (int i = 0; i <= 5; i++) {
                String val = rule.getField(i);
                if (val != null && !val.isEmpty()) {
                    line += "," + val;
                }
            }
            Helper.loadPolicyLine(line, model);
        });
    }

    @Override
    public void savePolicy(Model model) {
        repository.deleteAll().thenMany(
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
        ).flatMap(repository::save).subscribe();
    }

    @Override
    public void addPolicy(String sec, String ptype, List<String> rule) {
        CasbinRule casbinRule = new CasbinRule();
        casbinRule.setPtype(ptype);
        for (int i = 0; i < rule.size(); i++) {
            casbinRule.setField(i, rule.get(i));
        }
        repository.save(casbinRule).subscribe();
    }

    @Override
    public void removePolicy(String sec, String ptype, List<String> rule) {
        repository.deleteByPtypeAndFields(ptype, rule).subscribe();
    }

    @Override
    public void removeFilteredPolicy(String sec, String ptype, int fieldIndex, String... fieldValues) {
        repository.deleteByPtypeAndFieldIndex(ptype, fieldIndex, fieldValues).subscribe();
    }
}
