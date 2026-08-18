package com.crud_.residencia.services;

import com.crud_.residencia.domain.Usuario;
import com.crud_.residencia.dtos.UsuarioDTO;
import com.crud_.residencia.dtos.UsuarioResponseDTO;
import com.crud_.residencia.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(UUID id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuario.setNome(dto.nome());
        }

        if (dto.telefone() != null && !dto.telefone().isBlank()) {
            usuario.setTelefone(dto.telefone());
        }

        if (dto.dataNascimento() != null) {
            usuario.setDataNascimento(dto.dataNascimento());
        }

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario atualizado = repository.save(usuario);
        return new UsuarioResponseDTO(atualizado);
    }

    @Transactional
    public void deletar(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}