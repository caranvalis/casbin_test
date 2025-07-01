package com.casbin.casbin_test.config;

import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class CasbinConfig {

    private static final Logger log = LoggerFactory.getLogger(CasbinConfig.class);
    @Value("${casbin.model}")
    private String rbacModelPath;

    @Value("${casbin.policy}")
    private String rbacPolicyPath;

    private final ResourceLoader resourceLoader;

    public CasbinConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean (name = "rbacEnforcer")
    @Primary
    public Enforcer enforcer() throws IOException {
        System.out.println("Chargement du modèle RBAC : " + rbacModelPath);
        System.out.println("Chargement de la politique RBAC : " + rbacPolicyPath);

        Resource modelResource = resourceLoader.getResource(rbacModelPath);
        Resource policyResource = resourceLoader.getResource(rbacPolicyPath);

        System.out.println("Modèle trouvé : " + modelResource.exists());
        System.out.println("Politique trouvée : " + policyResource.exists());

        Enforcer enforcer = new Enforcer(modelResource.getFile().getAbsolutePath(), policyResource.getFile().getAbsolutePath());

        // Effacer toutes les politiques existantes
        enforcer.clearPolicy();

        // Ajouter des règles globales valides (3 paramètres)
        enforcer.addPolicy("admin", "/*", "*");
        enforcer.addPolicy("user", "/*", "*");
        enforcer.addPolicy("anonymous", "/*", "*");

        // Swagger et docs
        enforcer.addPolicy("anonymous", "/swagger-ui/**", "GET");
        enforcer.addPolicy("anonymous", "/v3/api-docs/**", "GET");
        enforcer.addPolicy("anonymous", "/webjars/**", "GET");
        enforcer.addRoleForUser("default_user","anonymous");

        // Affecter un rôle anonyme
        enforcer.addRoleForUser("default_user", "anonymous");

        enforcer.savePolicy();

        System.out.println("Politiques configurées pour accès libre");
        System.out.println("Nombre de politiques : " + enforcer.getPolicy().size());

        return enforcer;
    }
    @Bean(name = "abacEnforcer")
    public Enforcer abacEnforcer() throws IOException {
        Resource modelResource = resourceLoader.getResource("classpath:abac_model.conf");
        Resource policyResource = resourceLoader.getResource("classpath:abac_policy.csv");

        System.out.println("Chargement du modèle ABAC : " + modelResource.getFilename());
        System.out.println("Chargement de la politique ABAC : " + policyResource.getFilename());
        System.out.println("Modèle ABAC trouvé : " + modelResource.exists());
        System.out.println("Politique ABAC trouvée : " + policyResource.exists());

        return new Enforcer(modelResource.getFile().getAbsolutePath(), policyResource.getFile().getAbsolutePath());
    }
}
