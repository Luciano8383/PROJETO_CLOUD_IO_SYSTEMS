package com.senai.cloud_io.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.senai.cloud_io.model.PerfilUsuario;
import com.senai.cloud_io.model.StatusUsuario;
import com.senai.cloud_io.model.Usuario;
import com.senai.cloud_io.repository.UsuarioRepository;
import com.senai.cloud_io.service.Datas;

@Component
public class AdminInicial implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminInicial.class);

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final AppProperties props;

    public AdminInicial(UsuarioRepository repo, PasswordEncoder encoder, AppProperties props) {
        this.repo = repo;
        this.encoder = encoder;
        this.props = props;
    }

    @Override
    public void run(ApplicationArguments args) {
        var cfg = props.admin();
        if (cfg == null || cfg.email() == null || cfg.email().isBlank()) return;
        String email = cfg.email().trim().toLowerCase();
        if (repo.existsByEmailIgnoreCase(email)) return;

        Usuario admin = new Usuario();
        admin.setNome(cfg.nome());
        admin.setEmail(email);
        admin.setSenhaHash(encoder.encode(cfg.senha()));
        admin.setPerfil(PerfilUsuario.ADMIN);
        admin.setStatus(StatusUsuario.APROVADO);
        admin.setCriadoEm(Datas.agoraUtc());
        repo.save(admin);
        log.warn("Administrador inicial criado: {} (troque a senha via variável ADMIN_PASSWORD em produção)", email);
    }
}
