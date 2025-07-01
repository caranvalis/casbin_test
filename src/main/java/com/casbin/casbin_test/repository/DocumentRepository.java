package com.casbin.casbin_test.repository;

import com.casbin.casbin_test.model.Document;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DocumentRepository extends R2dbcRepository<Document, String> {
    Mono<Document> findByTitle(String title);
    Flux<Document> findByOwner(String owner);
    Flux<Document> findByCategory(String category);
}