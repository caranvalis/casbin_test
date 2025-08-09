package com.casbin.casbin_test.infrastructure.persistence.repository;

import com.casbin.casbin_test.infrastructure.persistence.entity.CasbinRuleEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SpringDataR2dbcCasbinRuleRepository extends ReactiveCrudRepository<CasbinRuleEntity, Long> {

    Flux<CasbinRuleEntity> findByPType(String pType);

    @Query("DELETE FROM casbin_rule WHERE p_type = :pType AND v0 = :v0 AND v1 = :v1 AND v2 = :v2")
    Mono<Void> deleteRule(String pType, String v0, String v1, String v2);

    @Query("DELETE FROM casbin_rule WHERE p_type = :pType")
    Mono<Void> deleteByPType(String pType);
}