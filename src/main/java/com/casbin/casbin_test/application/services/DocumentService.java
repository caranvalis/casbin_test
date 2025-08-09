package com.casbin.casbin_test.application.services;

import com.casbin.casbin_test.application.ports.input.DocumentUseCase;
import com.casbin.casbin_test.application.ports.output.DocumentRepository;
import com.casbin.casbin_test.domain.model.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DocumentService implements DocumentUseCase {
    private final DocumentRepository documentRepository;

    @Override
    public Mono<Document> findById(String id) {
        return documentRepository.findById(id);
    }

    @Override
    public Flux<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Mono<Document> save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return documentRepository.deleteById(id);
    }
}
