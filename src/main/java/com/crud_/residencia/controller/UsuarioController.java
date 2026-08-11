package com.crud_.residencia.controller;


import com.crud_.residencia.domain.Usuario;
import com.crud_.residencia.dtos.UsuarioDTO;
import com.crud_.residencia.repositories.UsuarioRepository;
import com.crud_.residencia.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios/")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("cadastro")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO){

        Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.nome())
                .email(usuarioDTO.email())
                .cpf(usuarioDTO.cpf())
                .telefone(usuarioDTO.telefone())
                .dataNascimento(usuarioDTO.dataNascimento())
                .build();

        usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.ok("USUARIO CADASTRADO");
    }

    @GetMapping("consultar/{id}")
    public ResponseEntity<UsuarioDTO> consultarUsuarioPorId (@PathVariable UUID id){

            UsuarioDTO usuario = usuarioService.buscarUsuarioPorId(id);


        return ResponseEntity.ok(usuario);
    }

    @GetMapping("consultar/listar")
    public ResponseEntity<List<Usuario>> listarUsuarios(){

        var usuarios = usuarioService.listarUsuarios();

        return ResponseEntity.ok(usuarios);

    }

    @PutMapping("atualizar/")
    public ResponseEntity<String> atualizarUsuario (@RequestParam  UUID id , @RequestBody UsuarioDTO usuarioDTO){

            Usuario usuario = Usuario.builder()
                    .id(usuarioDTO.id())
                    .email(usuarioDTO.email())
                    .nome(usuarioDTO.nome())
                    .cpf(usuarioDTO.cpf())
                    .telefone(usuarioDTO.telefone())
                    .dataNascimento(usuarioDTO.dataNascimento())
                    .dataCadastro(usuarioDTO.dataCadastro())
                    .build();

            usuarioService.atualizarPorId(id ,  usuario);
            return ResponseEntity.ok("USUARIO ATUALIZADO");


    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable UUID id){
        usuarioService.deletarUsuario(id);
        return ResponseEntity.ok("USUARIO DELETADO");
    }

}
