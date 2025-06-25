package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final Map<String, Document> documents = new ConcurrentHashMap<>();


    public List<Document> findAll() {
        return new ArrayList<>(documents.values());
    }

    public Document findById(String id) {
        return documents.get(id);
    }

    public Document save(Document document) {
        if (document.getId() == null || document.getId().isEmpty()) {
            document.setId(UUID.randomUUID().toString());
        }
        documents.put(document.getId(), document);
        return document;
    }

    public Document shareDocument(String documentId, String userId, String permission) {
        Document document = findById(documentId);
        if (document == null) {
            throw new IllegalArgumentException("Document non trouvé avec l'ID: " + documentId);
        }

        // Initialisation de la map de partage si elle n'existe pas
        if (document.getSharedWith() == null) {
            document.setSharedWith(new HashMap());
        }

        // Ajouter l'utilisateur avec la permission spécifiée
        document.getSharedWith().put(userId, permission);

        // Sauvegarder les modifications
        return update(document);
    }

    public Document update(Document document) {
        if (document.getId() == null || !documents.containsKey(document.getId())) {
            throw new IllegalArgumentException("Document introuvable pour la mise à jour");
        }
        documents.put(document.getId(), document);
        return document;
    }

    public void deleteById(String id) {
        documents.remove(id);
    }
}