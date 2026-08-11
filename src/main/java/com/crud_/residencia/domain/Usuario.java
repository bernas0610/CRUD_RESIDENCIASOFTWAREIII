package com.crud_.residencia.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
@Entity
@SoftDelete(columnName = "deletado")
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String nome;
    private String cpf;
    private String telefone;

    private LocalDate dataNascimento;

    @CreationTimestamp
    private LocalDateTime dataCadastro;



}
