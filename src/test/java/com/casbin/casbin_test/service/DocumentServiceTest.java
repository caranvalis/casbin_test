package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import com.casbin.casbin_test.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AuthorizationService authorizationService;

    @Test
    public void testCreateDocument() {
        // Créer un utilisateur
        User user = new User("alice", "alice@example.com", "ADMIN");

        // Créer un document
        Document document = new Document();
        document.setTitle("Test Document");
        document.setContent("Test Content");
        document.setCreatorId("alice");

        StepVerifier.create(documentService.save(document))
            .assertNext(savedDoc -> {
                assertNotNull(savedDoc.getId());
                assertEquals("Test Document", savedDoc.getTitle());
                assertEquals("Test Content", savedDoc.getContent());
                assertEquals("alice", savedDoc.getCreatorId());
            })
            .verifyComplete();
    }

    @Test
    public void testGetDocumentById() {
        // Créer un document
        Document document = new Document();
        document.setTitle("Document pour test");
        document.setContent("Contenu de test");
        document.setCreatorId("user1");

        StepVerifier.create(documentService.save(document)
            .flatMap(savedDoc -> documentService.findById(savedDoc.getId())))
            .assertNext(retrievedDoc -> {
                assertNotNull(retrievedDoc);
                assertEquals("Document pour test", retrievedDoc.getTitle());
                assertEquals("Contenu de test", retrievedDoc.getContent());
            })
            .verifyComplete();
    }

    // Adaptez les autres tests de la même manière...
}