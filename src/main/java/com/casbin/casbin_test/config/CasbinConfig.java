package com.casbin.casbin_test.config;

import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;
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

    @Value("${casbin.model}")
    private String rbacModelPath;

    @Value("${casbin.policy}")
    private String rbacPolicyPath;

    @Value("${casbin.abac.model:abac_model.conf}")
    private String abacModelPath;

    @Value("${casbin.abac.policy:abac_policy.csv}")
    private String abacPolicyPath;

    private final ResourceLoader resourceLoader;

    public CasbinConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @Primary
    public Enforcer enforcer() throws IOException {
        Resource modelResource = resourceLoader.getResource("classpath:" + rbacModelPath);
        Resource policyResource = resourceLoader.getResource("classpath:" + rbacPolicyPath);
        return new Enforcer(modelResource.getFile().getAbsolutePath(), policyResource.getFile().getAbsolutePath());
    }

    @Bean(name = "abacEnforcer")
    public Enforcer abacEnforcer() throws IOException {
        Resource modelResource = resourceLoader.getResource("classpath:" + abacModelPath);
        Resource policyResource = resourceLoader.getResource("classpath:" + abacPolicyPath);
        return new Enforcer(modelResource.getFile().getAbsolutePath(), policyResource.getFile().getAbsolutePath());
    }

    @Bean(name = "customEnforcer")
    public Enforcer customEnforcer() throws IOException {
        ClassPathResource modelResource = new ClassPathResource("model.conf");
        Model model = new Model();
        model.loadModelFromText(new String(Files.readAllBytes(
                Paths.get(modelResource.getURI())), StandardCharsets.UTF_8));

        ClassPathResource policyResource = new ClassPathResource("policy.csv");
        FileAdapter adapter = new FileAdapter(policyResource.getFile().getAbsolutePath());

        return new Enforcer(model, adapter);
    }
}