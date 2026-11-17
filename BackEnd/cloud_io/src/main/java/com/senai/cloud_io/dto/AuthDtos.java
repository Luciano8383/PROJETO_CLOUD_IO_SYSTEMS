package com.senai.cloud_io.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
    private AuthDtos() {}

    public record CadastroRequest(
            @NotBlank(message = "Informe o nome") @Size(max = 120, message = "Máximo de 120 caracteres") String nome,
            @NotBlank(message = "Informe o e-mail") @Email(message = "E-mail inválido")
            @Size(max = 160, message = "Máximo de 160 caracteres") String email,
            @NotBlank(message = "Informe a senha")
            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,72}$",
                    message = "A senha deve ter de 8 a 72 caracteres, com letras e números") String senha,
            @Size(max = 30, message = "Máximo de 30 caracteres") String matricula,
            @Size(max = 60, message = "Máximo de 60 caracteres") String turma) {}

    public record LoginRequest(
            @NotBlank(message = "Informe o e-mail") String email,
            @NotBlank(message = "Informe a senha") String senha) {}

    public record LoginResponse(String token, String tipo, long expiraEmSegundos, UsuarioDTO usuario) {}

    public record MensagemResponse(String mensagem) {}
}

