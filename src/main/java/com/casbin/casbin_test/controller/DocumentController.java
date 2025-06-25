package com.casbin.casbin_test.controller;

import com.casbin.casbin_test.model.Document;
import com.casbin.casbin_test.model.User;
import com.casbin.casbin_test.service.AbacAuthorizationService;
import com.casbin.casbin_test.service.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final AbacAuthorizationService authorizationService;
    private final DocumentServiceImpl documentService;

    @Autowired
    public DocumentController(AbacAuthorizationService authorizationService, DocumentServiceImpl documentService) {
        this.authorizationService = authorizationService;
        this.documentService = documentService;
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable String id) {
        Document document = documentService.findById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(document);
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@RequestBody Document document, @RequestAttribute("currentUser") User user) {
        // Définir l'utilisateur actuel comme créateur de contenu
        document.setCreatorId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.save(document));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(@PathVariable String id,
                                            @RequestBody Document document,
                                            @RequestAttribute("currentUser") User user) {
        Document existingDocument = documentService.findById(id);
        if (existingDocument == null) {
            return ResponseEntity.notFound().build();
        }

        // je ne sais vraimnent pas si il peut Vérificatier des autorisations avec notre service ABAC
        if (!authorizationService.canEditDocument(user, existingDocument)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Vous n'êtes pas autorisé à modifier ce document");
        }

        // Mise à jour du document si autorisé si il veut
        document.setId(id);
        document.setCreatorId(existingDocument.getCreatorId()); // Conserver le premier Donman
        return ResponseEntity.ok(documentService.update(document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable String id,
                                            @RequestAttribute("currentUser") User user) {
        Document document = documentService.findById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        if (!authorizationService.checkPermission(user, document, "delete")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Vous n'êtes pas autorisé à supprimer ce document");
        }

        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

        @PostMapping("/{id}/share")
        public ResponseEntity<?> shareDocument(@PathVariable String id,
                                               @RequestParam String userId,
                                               @RequestParam(defaultValue = "read") String permission,
                                               @RequestAttribute("currentUser") User currentUser) {
            Document document = documentService.findById(id);
            if (document == null) {
                return ResponseEntity.notFound().build();
            }

            // Vérifier que l'utilisateur actuel a le droit de partager le docs sinon il laisse
            if (!authorizationService.canShareDocument(currentUser, document)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Vous n'êtes pas autorisé à partager ce document");
            }

            documentService.shareDocument(id, userId, permission);
            return ResponseEntity.ok("Document partagé avec succès");
        }
}