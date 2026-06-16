package com.mmotores.m_motors_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "✅ Backend M-Motors fonctionne correctement ! (déployé sur le serveur)";
    }

    @GetMapping("/")
    public String home() {
        return "✅ Bienvenue sur M-Motors Backend !";
    }
}