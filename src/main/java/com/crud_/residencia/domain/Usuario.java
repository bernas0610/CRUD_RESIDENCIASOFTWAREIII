package com.crud_.residencia.domain;

import com.crud_.residencia.enums.UsuarioRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
@Entity
@SoftDelete(columnName = "deletado")
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false, length = 18)
    private String cpf;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UsuarioRole role;

    private LocalDate dataNascimento;

    @CreationTimestamp
    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @UpdateTimestamp
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @PrePersist
    @PreUpdate
    private void formatarDadosParaBanco() {
        if (this.cpf != null) {
            this.cpf = this.cpf.replaceAll("\\D", "");
        }
        if (this.telefone != null) {
            this.telefone = this.telefone.replaceAll("\\D", "");
        }
    }

    private String mascararCpf(String valor) {
        if (valor == null) return null;
        String limpo = valor.replaceAll("\\D", "");
        // Se já tiver 11 dígitos, aplica a máscara de armazenamento
        if (limpo.length() == 11) {
            return "***." + limpo.substring(3, 6) + "." + limpo.substring(6, 9) + "-**";
            // Nota: Se a sua intenção for máscara de pontuação (093.081.085-64), use:
            // return limpo.substring(0, 3) + "." + limpo.substring(3, 6) + "." + limpo.substring(6, 9) + "-" + limpo.substring(9);
        }
        return valor;
    }

    private String mascararTelefone(String valor) {
        if (valor == null) return null;
        String limpo = valor.replaceAll("\\D", "");
        // Formato para 11 dígitos (DDD + 9 dígitos)
        if (limpo.length() == 11) {
            return "(" + limpo.substring(0, 2) + ") *****-" + limpo.substring(7);
            // Nota: Se a intenção for apenas formatação (79) 99958-7554, use:
            // return "(" + limpo.substring(0, 2) + ") " + limpo.substring(2, 7) + "-" + limpo.substring(7);
        }
        return valor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UsuarioRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}