package com.casbin.casbin_test.application.ports.ouput;

import com.casbin.casbin_test.domain.model.CasbinRule;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface CasbinRuleRepository extends R2dbcRepository<CasbinRule, Long> {

    Flux<CasbinRule> findByPtype(String ptype);

    Flux<CasbinRule> findByV0(String v0);

    Mono<Void> deleteByPtypeAndV0AndV1AndV2(String ptype, String v0, String v1, String v2);

    // 🔹 Supprimer une règle complète (jusqu’à 6 champs)
    @Query("""
        DELETE FROM casbin_rule 
        WHERE ptype = :ptype
          AND COALESCE(v0, '') = COALESCE(:#{#fields[0]}, '')
          AND COALESCE(v1, '') = COALESCE(:#{#fields[1]}, '')
          AND COALESCE(v2, '') = COALESCE(:#{#fields[2]}, '')
          AND COALESCE(v3, '') = COALESCE(:#{#fields[3]}, '')
          AND COALESCE(v4, '') = COALESCE(:#{#fields[4]}, '')
          AND COALESCE(v5, '') = COALESCE(:#{#fields[5]}, '')
        """)
    Mono<Void> deleteByPtypeAndFields(String ptype, List<String> fields);

    // 🔹 Supprimer via un filtre à partir d’un index
    @Query("""
        DELETE FROM casbin_rule
        WHERE ptype = :ptype AND (
            (:fieldIndex = 0 AND v0 = :#{#fieldValues[0]}) OR
            (:fieldIndex = 1 AND v1 = :#{#fieldValues[0]}) OR
            (:fieldIndex = 2 AND v2 = :#{#fieldValues[0]}) OR
            (:fieldIndex = 3 AND v3 = :#{#fieldValues[0]}) OR
            (:fieldIndex = 4 AND v4 = :#{#fieldValues[0]}) OR
            (:fieldIndex = 5 AND v5 = :#{#fieldValues[0]})
        )
        """)
    Mono<Void> deleteByPtypeAndFieldIndex(String ptype, int fieldIndex, String[] fieldValues);
}
