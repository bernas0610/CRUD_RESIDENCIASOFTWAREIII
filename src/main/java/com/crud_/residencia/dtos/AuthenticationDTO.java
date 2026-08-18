package com.crud_.residencia.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank String email,
        @NotBlank String senha
) {}