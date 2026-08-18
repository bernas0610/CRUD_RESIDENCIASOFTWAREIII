package com.crud_.residencia.controller;

import com.crud_.residencia.domain.Usuario;
import com.crud_.residencia.dtos.AuthenticationDTO;
import com.crud_.residencia.dtos.LoginResponseDTO;
import com.crud_.residencia.dtos.RegisterDTO;
import com.crud_.residencia.repositories.UsuarioRepository;
import com.crud_.residencia.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Usuario newUser = Usuario.builder()
                .nome(data.nome())
                .email(data.email())
                .cpf(data.cpf())
                .telefone(data.telefone())
                .senha(encryptedPassword)
                .role(data.role())
                .dataNascimento(data.dataNascimento())
                .build();

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}