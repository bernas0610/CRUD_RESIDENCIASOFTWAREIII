package com.crud_.residencia.dtos;

import java.time.LocalDate;

public record UsuarioDTO(
        String nome,
        String telefone,
        String senha,
        LocalDate dataNascimento
) {}