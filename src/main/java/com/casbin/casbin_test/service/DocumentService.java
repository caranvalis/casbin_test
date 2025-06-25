package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import java.util.List;

public interface DocumentService {
    List<Document> findAll();
    Document findById(String id);
    Document save(Document document);
    Document update(Document document);
    void deleteById(String id);
}