// DocumentService.java
package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DocumentService {
    Mono<Document> save(Document document);
    Mono<Document> findById(String id);
    Flux<Document> findAll();
    Mono<Void> deleteById(String id);
    Mono<Void> delete(String id);
    Mono<Document> shareDocument(String documentId, String userId, String permission);
    Flux<Document> findByOwner(String owner);
}
