package com.casbin.casbin_test.infrastructure.persistence.repository;

import com.casbin.casbin_test.infrastructure.persistence.entity.DocumentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataR2dbcDocumentRepository extends ReactiveCrudRepository<DocumentEntity, String> {
    // Méthodes supplémentaires spécifiques si nécessaires
}
