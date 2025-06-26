package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DocumentService {
    Flux<Document> findAll();
    Mono<Document> findById(String id);
    Mono<Document> save(Document document);
    Mono<Document> update(Document document);
    Mono<Boolean> shareDocument(String documentId, String userId, String permission);
    Flux<Document> searchByKeyword(String keyword);
    Flux<Document> findByCategory(String categoryId);
    Mono<Void> deleteById(String id);
}