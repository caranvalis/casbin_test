package com.casbin.casbin_test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public Mono<String> hello(@RequestHeader("X-User") String user) {
        return Mono.just("Bienvenue " + user + " !");
    }
}
