package com.crud_.residencia.dtos;

import com.crud_.residencia.domain.Usuario;
import com.crud_.residencia.enums.UsuarioRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String cpf,
        String telefone,
        UsuarioRole role,
        LocalDate dataNascimento,
        LocalDateTime dataCadastro,
        LocalDateTime dataModificacao
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                mascararCpf(usuario.getCpf()),
                mascararTelefone(usuario.getTelefone()),
                usuario.getRole(),
                usuario.getDataNascimento(),
                usuario.getDataCadastro(),
                usuario.getDataModificacao()
        );
    }

    private static String mascararCpf(String valor) {
        if (valor == null) return null;
        String limpo = valor.replaceAll("\\D", "");
        if (limpo.length() == 11) {
            return "***." + limpo.substring(3, 6) + "." + limpo.substring(6, 9) + "-**";
        }
        return valor;
    }

    private static String mascararTelefone(String valor) {
        if (valor == null) return null;
        String limpo = valor.replaceAll("\\D", "");
        if (limpo.length() == 11) {
            return "(" + limpo.substring(0, 2) + ") *****-" + limpo.substring(7);
        }
        return valor;
    }
}