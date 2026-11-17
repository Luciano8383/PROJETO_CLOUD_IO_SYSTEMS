package com.senai.cloud_io.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.senai.cloud_io.config.ApiException;
import com.senai.cloud_io.dto.UsuarioDTO;
import com.senai.cloud_io.model.StatusUsuario;
import com.senai.cloud_io.model.Usuario;
import com.senai.cloud_io.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Usuario atual(Authentication authentication) {
        return repo.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Sessão inválida. Entre novamente."));
    }

    public List<UsuarioDTO> listar(StatusUsuario status) {
        List<Usuario> lista = status == null ? repo.findAllByOrderByCriadoEmDesc()
                : repo.findByStatusOrderByCriadoEmDesc(status);
        return lista.stream().map(UsuarioDTO::de).toList();
    }

    public UsuarioDTO alterarStatus(Long id, StatusUsuario novoStatus, Usuario quemAltera) {
        Usuario alvo = repo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        if (alvo.getId().equals(quemAltera.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Você não pode alterar o seu próprio acesso.");
        }
        alvo.setStatus(novoStatus);
        return UsuarioDTO.de(repo.save(alvo));
    }
}

