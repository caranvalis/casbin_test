package com.casbin.casbin_test.infrastructure.persistence.mapper;

import com.casbin.casbin_test.infrastructure.persistence.entity.CasbinRuleEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

@Component
public class CasbinRuleMapper {

    public Mono<List<String>> toPolicyReactive(CasbinRuleEntity entity) {
        return Mono.fromCallable(() -> {
            List<String> values = new ArrayList<>();
            if (entity.getV0() != null) values.add(entity.getV0());
            if (entity.getV1() != null) values.add(entity.getV1());
            if (entity.getV2() != null) values.add(entity.getV2());
            if (entity.getV3() != null) values.add(entity.getV3());
            if (entity.getV4() != null) values.add(entity.getV4());
            if (entity.getV5() != null) values.add(entity.getV5());
            return values;
        });
    }

    public Mono<CasbinRuleEntity> toEntityReactive(String ptype, List<String> rule) {
        return Mono.fromCallable(() -> {
            CasbinRuleEntity entity = new CasbinRuleEntity();
            entity.setPType(ptype);

            if (rule.size() > 0) entity.setV0(rule.get(0));
            if (rule.size() > 1) entity.setV1(rule.get(1));
            if (rule.size() > 2) entity.setV2(rule.get(2));
            if (rule.size() > 3) entity.setV3(rule.get(3));
            if (rule.size() > 4) entity.setV4(rule.get(4));
            if (rule.size() > 5) entity.setV5(rule.get(5));

            return entity;
        });
    }

    // Méthodes synchrones pour les cas d'usage simples
    public List<String> toPolicy(CasbinRuleEntity entity) {
        List<String> values = new ArrayList<>();
        if (entity.getV0() != null) values.add(entity.getV0());
        if (entity.getV1() != null) values.add(entity.getV1());
        if (entity.getV2() != null) values.add(entity.getV2());
        if (entity.getV3() != null) values.add(entity.getV3());
        if (entity.getV4() != null) values.add(entity.getV4());
        if (entity.getV5() != null) values.add(entity.getV5());
        return values;
    }

    public CasbinRuleEntity toEntity(String ptype, List<String> rule) {
        CasbinRuleEntity entity = new CasbinRuleEntity();
        entity.setPType(ptype);

        if (rule.size() > 0) entity.setV0(rule.get(0));
        if (rule.size() > 1) entity.setV1(rule.get(1));
        if (rule.size() > 2) entity.setV2(rule.get(2));
        if (rule.size() > 3) entity.setV3(rule.get(3));
        if (rule.size() > 4) entity.setV4(rule.get(4));
        if (rule.size() > 5) entity.setV5(rule.get(5));

        return entity;
    }
}
