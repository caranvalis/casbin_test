package com.casbin.casbin_test.application.ports.output;

import com.casbin.casbin_test.domain.model.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DocumentRepository {
    Mono<Document> findById(String id);
    Flux<Document> findAll();
    Mono<Document> save(Document document);
    Mono<Void> deleteById(String id);
}
