package com.crud_.residencia.dtos;

import com.crud_.residencia.enums.UsuarioRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RegisterDTO(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String cpf,
        String telefone,
        @NotBlank String senha,
        @NotNull UsuarioRole role,
        LocalDate dataNascimento
) {}