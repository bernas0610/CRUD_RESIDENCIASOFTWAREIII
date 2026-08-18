package com.crud_.residencia.repositories;

import com.crud_.residencia.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario , UUID> {

    Optional<Usuario> findByNome(String nome);

    List<Usuario> findAll();
    UserDetails findByEmail(String email);





}
