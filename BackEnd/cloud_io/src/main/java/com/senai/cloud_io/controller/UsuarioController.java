package com.senai.cloud_io.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.senai.cloud_io.dto.UsuarioDTO;
import com.senai.cloud_io.model.StatusUsuario;
import com.senai.cloud_io.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public UsuarioDTO me(Authentication authentication) {
        return UsuarioDTO.de(service.atual(authentication));
    }

    @GetMapping
    public List<UsuarioDTO> listar(@RequestParam(required = false) StatusUsuario status) {
        return service.listar(status);
    }
}
