package com.crud_.residencia.services;


import com.crud_.residencia.domain.Usuario;
import com.crud_.residencia.dtos.UsuarioDTO;
import com.crud_.residencia.repositories.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public void cadastrarUsuario(Usuario usuario){

        if (usuario.getNome() == null || usuario.getCpf().isEmpty() || usuario.getEmail().isEmpty() || usuario.getTelefone().isEmpty() ){
            throw new RuntimeException("Preencha todos os campos obrigatórios");
        }





        usuarioRepository.saveAndFlush(usuario);
    }

    public UsuarioDTO buscarUsuarioPorId(UUID id){

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(
                        ()->  new RuntimeException("USUARIO NAO ENCONTRADO!")
                );

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getDataNascimento(),
                usuario.getDataCadastro()

        );




    }

    public List<Usuario> listarUsuarios(){

        return usuarioRepository.findAll();
    }

    public void atualizarPorId(UUID id , Usuario usuario){

        Usuario usuarioEntity = usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Id nao encontrado")
        );
        Usuario usuarioAtualizado = Usuario.builder()
                .email(usuario.getEmail() != null ? usuario.getEmail() : usuarioEntity.getEmail())
                .nome(usuario.getNome() != null ? usuario.getNome() : usuarioEntity.getNome())
                .id(usuarioEntity.getId())
                .telefone(usuario.getTelefone() != null ? usuario.getTelefone() : usuarioEntity.getTelefone())
                .cpf(usuario.getCpf() != null ? usuario.getCpf() : usuarioEntity.getCpf())
                .dataCadastro(usuario.getDataCadastro() != null ? usuario.getDataCadastro() : usuarioEntity.getDataCadastro())
                .dataNascimento(usuario.getDataNascimento() != null ? usuario.getDataNascimento() : usuarioEntity.getDataNascimento())
                .build();

        usuarioRepository.saveAndFlush(usuarioAtualizado);
    }

    public void deletarUsuario(UUID id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USUARIO NAO ENCONTRADO!"));
        usuarioRepository.delete(usuario);
    }




}
