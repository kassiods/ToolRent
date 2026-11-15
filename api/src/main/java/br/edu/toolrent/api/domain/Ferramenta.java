package br.edu.toolrent.api.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Ferramenta {

    private UUID id;
    private String codigo;
    private String nome;
    private String descricao;
    private BigDecimal tarifaDiaria;
    private int quantidade;
    private SituacaoFerramenta situacao;
    private boolean ativo;
    private OffsetDateTime criadoEm;

    public Ferramenta() {
    }

    public Ferramenta(UUID id, String codigo, String nome, String descricao,
                      BigDecimal tarifaDiaria, int quantidade,
                      SituacaoFerramenta situacao, boolean ativo,
                      OffsetDateTime criadoEm) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.tarifaDiaria = tarifaDiaria;
        this.quantidade = quantidade;
        this.situacao = situacao;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(BigDecimal tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public SituacaoFerramenta getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoFerramenta situacao) {
        this.situacao = situacao;
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
