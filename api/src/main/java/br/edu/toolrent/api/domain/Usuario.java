package br.edu.toolrent.api.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Usuario {

    private UUID id;
    private UUID authUserId;
    private String nome;
    private String email;
    private PerfilUsuario perfil;
    private boolean ativo;
    private OffsetDateTime criadoEm;

    public Usuario() {
    }

    public Usuario(UUID id, UUID authUserId, String nome, String email,
                   PerfilUsuario perfil, boolean ativo, OffsetDateTime criadoEm) {
        this.id = id;
        this.authUserId = authUserId;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(UUID authUserId) {
        this.authUserId = authUserId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
