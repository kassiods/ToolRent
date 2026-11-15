package br.edu.toolrent.api.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Categoria {

    private UUID id;
    private String nome;
    private String descricao;
    private OffsetDateTime criadoEm;

    public Categoria() {
    }

    public Categoria(UUID id, String nome, String descricao, OffsetDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
