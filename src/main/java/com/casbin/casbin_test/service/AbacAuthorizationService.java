package com.casbin.casbin_test.service;

import com.casbin.casbin_test.model.Document;
import com.casbin.casbin_test.model.User;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbacAuthorizationService {
    private final Enforcer enforcer;

    @Autowired
    public AbacAuthorizationService (Enforcer enforcer){
            this.enforcer = enforcer;
    }

    public boolean canEditDocument(User user, Document document){
    return enforcer.enforce(user, document,"edit");
    }

    public boolean checkPermission(User user, Document document, String action) {
        return enforcer.enforce(user, document, action);
    }

    public boolean canShareDocument(User currentUser, Document document) {
        return enforcer.enforce(currentUser, document, "share");
    }
}
