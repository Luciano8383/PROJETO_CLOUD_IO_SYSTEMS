package com.senai.cloud_io.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.senai.cloud_io.dto.AuthDtos.CadastroRequest;
import com.senai.cloud_io.dto.AuthDtos.LoginRequest;
import com.senai.cloud_io.dto.AuthDtos.LoginResponse;
import com.senai.cloud_io.dto.AuthDtos.MensagemResponse;
import com.senai.cloud_io.dto.UsuarioDTO;
import com.senai.cloud_io.service.AuthService;
import com.senai.cloud_io.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService auth;
    private final UsuarioService usuarios;

    public AuthController(AuthService auth, UsuarioService usuarios) {
        this.auth = auth;
        this.usuarios = usuarios;
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MensagemResponse cadastrar(@Valid @RequestBody CadastroRequest req) {
        return auth.cadastrar(req);
    }

    @PostMapping("/auth/login")
    public LoginResponse entrar(@Valid @RequestBody LoginRequest req) {
        return auth.entrar(req);
    }

    @GetMapping("/auth/me")
    public UsuarioDTO eu(Authentication authentication) {
        return UsuarioDTO.de(usuarios.atual(authentication));
    }

    @GetMapping("/health")
    public Map<String, String> saude() {
        return Map.of("status", "ok");
    }
}