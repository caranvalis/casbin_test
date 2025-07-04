package com.casbin.casbin_test.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Map;

@Table("documents")
public class Document {

    @Id
    private String id;
    private String title;
    private String content;
    private String owner;
    private String category; // Ajout de la propriété manquante
    private String sharedWithJson = "{}"; //savoir a qui est partagé le document
    private String permissionsJson = "{}";// savoir les permissions de chaque utilisateur sur le document

    // Constructeurs
    public Document() {}

    public Document(String title, String content, String owner, String category) {
        this.title = title;
        this.content = content;
        this.owner = owner;
        this.category = category;
        this.sharedWithJson = "{}";
        this.permissionsJson = "{}";
    }

    // Getters et Setters complets
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSharedWithJson() { return sharedWithJson; }
    public void setSharedWithJson(String sharedWithJson) { this.sharedWithJson = sharedWithJson; }

    public String getPermissionsJson() { return permissionsJson; }
    public void setPermissionsJson(String permissionsJson) { this.permissionsJson = permissionsJson; }

    // Méthodes utilitaires simplifiées
    public boolean isOwnedBy(String userId) {
        return this.owner != null && this.owner.equals(userId);
    }

    public boolean isSharedWith(String userId) {
        return this.sharedWithJson != null && this.sharedWithJson.contains(userId);
    }
    public void shareWith(String userId, String permission) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> sharedWith = objectMapper.readValue(this.sharedWithJson, Map.class);
            sharedWith.put(userId, permission);
            this.sharedWithJson = objectMapper.writeValueAsString(sharedWith);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la mise à jour des permissions de partage", e);
        }
    }
}