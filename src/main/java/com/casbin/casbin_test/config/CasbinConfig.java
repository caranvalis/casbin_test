package com.casbin.casbin_test.config;

import com.casbin.casbin_test.adapter.R2dbcCasbinAdapter;
import com.casbin.casbin_test.model.Resource;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.flywaydb.core.internal.resource.classpath.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class CasbinConfig {

    private static final Logger log = LoggerFactory.getLogger(CasbinConfig.class);

    // Modèle RBAC amélioré pour supporter les jokers (*)
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
        m = g(r.sub, p.sub) || r.sub == p.sub && (r.obj == p.obj || p.obj == '*') && (r.act == p.act || p.act == '*')
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

    @Bean(name = "rbacEnforcer")
    @Primary
    public Enforcer rbacEnforcer(R2dbcCasbinAdapter adapter) {
        log.info("Initialisation du modèle RBAC Casbin depuis String");

        Model model = new Model();
        model.loadModelFromText(RBAC_MODEL_TEXT);

        Enforcer enforcer = new Enforcer(model, adapter);
        enforcer.enableAutoSave(true);
        enforcer.loadPolicy();

        // Afficher les règles actuellement chargées
        log.info("Politiques RBAC chargées: {}", enforcer.getPolicy().size());
        enforcer.getPolicy().forEach(policy ->
            log.info("Politique: {}", String.join(", ", policy)));

        return enforcer;
    }

    @Bean(name = "abacEnforcer")
    public Enforcer abacEnforcer() {
        log.info("Initialisation du modèle ABAC Casbin depuis String");

        Model model = new Model();
        model.loadModelFromText(ABAC_MODEL_TEXT);

        Enforcer enforcer = new Enforcer(model);
        enforcer.addPolicy("read");
        enforcer.addPolicy("write");
        enforcer.addPolicy("delete");
        return enforcer;
    }



    @Bean
    public CommandLineRunner initPolicies(Enforcer enforcer) {
        return args -> {
            log.info("Initialisation des politiques Casbin...");

            // Suppression des politiques existantes pour éviter les doublons
            enforcer.clearPolicy();

            // Politiques pour administrateurs
            enforcer.addPolicy("admin", "*", "*");
            enforcer.addPolicy("admin", "/api/documents", "*");
            enforcer.addPolicy("admin", "/api/users", "*");
            enforcer.addPolicy("admin", "/api/casbin", "*");
            enforcer.addPolicy("admin", "/api/hello", "*");

            // Politiques pour utilisateurs standard
            enforcer.addPolicy("public", "/api/documents", "GET");
            enforcer.addPolicy("public", "/api/documents", "POST");


            // Politiques pour utilisateurs anonymes
            enforcer.addPolicy("default_user", "/api/hello", "GET");

            // Relations de rôles
            enforcer.addRoleForUser("david", "user");

            // Sauvegarde des politiques
            enforcer.savePolicy();

            enforcer.loadPolicy();

            // Vérification
            log.info("Politiques initialisées: {}", enforcer.getPolicy().size());
            log.info("Relations de rôles: {}", enforcer.getGroupingPolicy().size());

            // Afficher toutes les politiques pour débogage
            enforcer.getPolicy().forEach(policy ->
                log.info("Politique: {}", String.join(", ", policy)));
        };
    }
}