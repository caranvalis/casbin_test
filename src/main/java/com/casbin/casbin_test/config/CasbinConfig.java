package com.casbin.casbin_test.config;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class CasbinConfig {

    @Value("${casbin.model}")
    private String modelPath;

    @Value("${casbin.policy}")
    private String policyPath;

    private final ResourceLoader resourceLoader;

    public CasbinConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Enforcer enforcer() throws IOException {
        Resource modelResource = resourceLoader.getResource("classpath:" + modelPath);
        Resource policyResource = resourceLoader.getResource("classpath:" + policyPath);
        return new Enforcer(modelResource.getFile().getAbsolutePath(), policyResource.getFile().getAbsolutePath());
    }
}
