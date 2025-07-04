package com.casbin.casbin_test.config;

import com.casbin.casbin_test.adapter.R2dbcCasbinAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@Configuration
public class CasbinConfig {

    private static final Logger log = LoggerFactory.getLogger(CasbinConfig.class);

    private static final String RBAC_MODEL_TEXT = """
        [request_definition]
        r = sub, obj, act

        [policy_definition]
        p = sub, obj, act

        [role_definition]
        g = _, _

        [policy_effect]
        e = some(where (p.eft == allow))

        [matchers]
        m = g(r.sub, p.sub) && r.obj == p.obj && r.act == p.act
        """;

    private static final String ABAC_MODEL_TEXT = """
        [request_definition]
        r = sub, obj, act

        [policy_definition]
        p = act

        [policy_effect]
        e = some(where (p.eft == allow))

        [matchers]
        m = r.sub == obj.owner && r.act == p.act
        """;

    // RBAC : pour API simple rôle + URI + action
    @Bean(name = "rbacEnforcer")
    @Primary
    public Enforcer rbacEnforcer(R2dbcCasbinAdapter adapter) {
        log.info("Initialisation du modèle RBAC Casbin depuis String");

        Model model = new Model();
        model.loadModelFromText(RBAC_MODEL_TEXT);

        Enforcer enforcer = new Enforcer(model, adapter);
        enforcer.enableAutoSave(true);
        enforcer.loadPolicy();

        return enforcer;
    }

    // ABAC : pour contexte plus riche (avec attributs dynamiques)
    @Bean(name = "abacEnforcer")
    public Enforcer abacEnforcer() {
        log.info("Initialisation du modèle ABAC Casbin depuis String");

        Model model = new Model();
        model.loadModelFromText(ABAC_MODEL_TEXT);

        Enforcer enforcer = new Enforcer(model);
        enforcer.addPolicy("read");
        return enforcer;
    }
    @Bean
    public CommandLineRunner initPolicies(Enforcer enforcer) {
        return args -> {
            // Ajoute les permissions admin -> * -> *
            boolean added = enforcer.addPermissionForUser("admin", "*", "*");
            if (added) {
                System.out.println("Permission admin -> * -> * ajoutée avec succès");
            } else {
                System.out.println("Permission admin -> * -> * déjà existante");
            }

            // Recharge les politiques après ajout
            enforcer.savePolicy();
        };
    }

}