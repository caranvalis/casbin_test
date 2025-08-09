
package com.casbin.casbin_test.infrastructure.persistence.repository;

import com.casbin.casbin_test.application.ports.output.DocumentRepository;
import com.casbin.casbin_test.domain.model.Document;
import com.casbin.casbin_test.infrastructure.persistence.entity.DocumentEntity;
import com.casbin.casbin_test.infrastructure.persistence.mapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class R2dbcDocumentRepository implements DocumentRepository {
    private final SpringDataR2dbcDocumentRepository repository;
    private final DocumentMapper mapper;

    @Override
    public Mono<Document> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Document> findAll() {
        return repository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Document> save(Document document) {
        DocumentEntity entity = mapper.toEntity(document);
        return repository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }
}
