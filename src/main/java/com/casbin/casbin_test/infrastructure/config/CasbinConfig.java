package com.casbin.casbin_test.infrastructure.config;

import com.casbin.casbin_test.infrastructure.persistence.adapter.R2dbcCasbinAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
        m = g(r.sub, p.sub) || r.sub == p.sub && (r.obj == p.obj || p.obj == '*') && (r.act == p.act || p.act == '*')
        """;

    private static final String ABAC_MODEL_TEXT = """
        [request_definition]
        r = sub, obj, act

        [policy_definition]
        p = sub, obj, act

        [policy_effect]
        e = some(where (p.eft == allow))

        [matchers]
        m = r.sub.age > 18 && r.obj == p.obj && r.act == p.act
        """;


    @Bean
    @Primary
    public Enforcer rbacEnforcer(R2dbcCasbinAdapter adapter, Flyway flyway) {
        log.info("Initialisation du modèle RBAC Casbin depuis String");
        Model model = new Model();
        model.loadModelFromText(RBAC_MODEL_TEXT);

        Enforcer enforcer = new Enforcer(model, adapter);
        enforcer.enableAutoSave(true);
        return enforcer;
    }

    @Bean("abacEnforcer")
    public Enforcer abacEnforcer(R2dbcCasbinAdapter adapter, Flyway flyway) {
        log.info("Initialisation du modèle ABAC Casbin depuis String");
        Model model = new Model();
        model.loadModelFromText(ABAC_MODEL_TEXT);

        Enforcer enforcer = new Enforcer(model, adapter);
        enforcer.enableAutoSave(true);
        return enforcer;
    }

    @Bean
    public CommandLineRunner initPolicies(Enforcer rbacEnforcer) {
        return args -> {
            log.info("Initialisation des politiques Casbin...");

            rbacEnforcer.loadPolicy();
            log.info("Politiques chargées au démarrage: {}", rbacEnforcer.getPolicy().size());

            rbacEnforcer.clearPolicy();

            rbacEnforcer.addPolicy("admin", "*", "*");
            rbacEnforcer.addPolicy("admin", "/api/documents", "*");
            rbacEnforcer.addPolicy("admin", "/api/users", "*");
            rbacEnforcer.addPolicy("admin", "/api/casbin", "*");
            rbacEnforcer.addPolicy("admin", "/api/hello", "*");

            rbacEnforcer.addPolicy("public", "/api/documents", "GET");
            rbacEnforcer.addPolicy("public", "/api/documents", "POST");

            rbacEnforcer.addPolicy("default_user", "/api/hello", "GET");

            rbacEnforcer.addRoleForUser("david", "user");

            rbacEnforcer.savePolicy();

            log.info("Politiques initialisées et sauvegardées: {}", rbacEnforcer.getPolicy().size());
            log.info("Relations de rôles: {}", rbacEnforcer.getGroupingPolicy().size());

            rbacEnforcer.getPolicy().forEach(policy ->
                    log.info("Politique: {}", String.join(", ", policy))
            );
        };
    }
}