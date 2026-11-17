package com.senai.cloud_io.dto;

import java.time.Instant;

import com.senai.cloud_io.model.Usuario;
import com.senai.cloud_io.service.Datas;

public record UsuarioDTO(Long id, String nome, String email, String matricula, String turma,
                         String perfil, String status, Instant criadoEm) {
    public static UsuarioDTO de(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNome(), u.getEmail(), u.getMatricula(), u.getTurma(),
                u.getPerfil().name(), u.getStatus().name(), Datas.paraInstant(u.getCriadoEm()));
    }
}
