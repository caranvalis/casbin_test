package com.casbin.casbin_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"server.port=0"})
@AutoConfigureWebTestClient
public class SwaggerUITest {

    @Autowired
    private WebTestClient webTestClient;

@Test
public void testSwaggerUIAccess() {
    webTestClient.get()
            .uri("/swagger-ui.html")
            .exchange()
            .expectStatus().isFound()  // 302 Found au lieu de isOk()
            .expectHeader().valueEquals("Location", "/webjars/swagger-ui/index.html");
}

    @Test
    public void testOpenAPIDocsAccess() {
        webTestClient.get()
                .uri("/v3/api-docs")
                .exchange()
                .expectStatus().isOk();
    }
}