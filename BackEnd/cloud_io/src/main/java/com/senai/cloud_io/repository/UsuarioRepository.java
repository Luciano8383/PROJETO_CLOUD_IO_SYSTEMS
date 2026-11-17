package com.senai.cloud_io.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.senai.cloud_io.model.StatusUsuario;
import com.senai.cloud_io.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    List<Usuario> findAllByOrderByCriadoEmDesc();
    List<Usuario> findByStatusOrderByCriadoEmDesc(StatusUsuario status);
}

