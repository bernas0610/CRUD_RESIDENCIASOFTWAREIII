package com.crud_.residencia.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioDTO(UUID id , String email , String nome , String telefone , String cpf , LocalDate dataNascimento , LocalDateTime dataCadastro) {
}
