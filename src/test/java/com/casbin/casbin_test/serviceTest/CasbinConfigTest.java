package com.casbin.casbin_test.serviceTest;

import org.casbin.jcasbin.main.Enforcer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CasbinConfigTest {

    @Autowired
    private Enforcer enforcer;

    @Test
    void testEnforcerBeanCreation() {
        assertNotNull(enforcer, "Le bean Enforcer n'a pas été créé");
    }
}
