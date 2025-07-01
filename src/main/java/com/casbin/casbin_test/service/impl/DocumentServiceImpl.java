package com.casbin.casbin_test.service.impl;

import com.casbin.casbin_test.model.Document;
import com.casbin.casbin_test.repository.DocumentRepository;
import com.casbin.casbin_test.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Mono<Document> save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Mono<Document> findById(String id) {
        return documentRepository.findById(id);
    }

    @Override
    public Flux<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Mono<Void> delete(String id) {
        return documentRepository.deleteById(id);
    }

    @Override
    public Mono<Document> shareDocument(String documentId, String userId, String permission) {
        return documentRepository.findById(documentId)
                .flatMap(document -> {
                    document.shareWith(userId, permission);
                    return documentRepository.save(document);
                });
    }

    @Override
    public Flux<Document> findByOwner(String owner) {
        return documentRepository.findByOwner(owner);
    }
}