package com.senai.cloud_io.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.senai.cloud_io.dto.UsuarioDTO;
import com.senai.cloud_io.model.StatusUsuario;
import com.senai.cloud_io.service.UsuarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {

    public record AlterarStatusRequest(@NotNull(message = "Informe o status") StatusUsuario status) {}

    private final UsuarioService service;

    public AdminUsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioDTO> listar(@RequestParam(required = false) StatusUsuario status) {
        return service.listar(status);
    }

    @PatchMapping("/{id}/status")
    public UsuarioDTO alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlterarStatusRequest req,
            Authentication authentication) {
        return service.alterarStatus(id, req.status(), service.atual(authentication));
    }
}
