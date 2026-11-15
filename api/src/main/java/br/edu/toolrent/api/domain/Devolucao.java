package br.edu.toolrent.api.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Devolucao {

    private UUID id;
    private OffsetDateTime dataDevolucao;
    private String condicao;
    private String descricaoDanos;
    private BigDecimal valorDanos;
    private boolean danosAprovados;
    private BigDecimal valorMulta;
    private Usuario registradoPor;
    private OffsetDateTime criadoEm;

    public Devolucao() {
    }

    public Devolucao(UUID id, OffsetDateTime dataDevolucao, String condicao,
                     String descricaoDanos, BigDecimal valorDanos, boolean danosAprovados,
                     BigDecimal valorMulta, Usuario registradoPor, OffsetDateTime criadoEm) {
        this.id = id;
        this.dataDevolucao = dataDevolucao;
        this.condicao = condicao;
        this.descricaoDanos = descricaoDanos;
        this.valorDanos = valorDanos;
        this.danosAprovados = danosAprovados;
        this.valorMulta = valorMulta;
        this.registradoPor = registradoPor;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(OffsetDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public String getDescricaoDanos() {
        return descricaoDanos;
    }

    public void setDescricaoDanos(String descricaoDanos) {
        this.descricaoDanos = descricaoDanos;
    }

    public BigDecimal getValorDanos() {
        return valorDanos;
    }

    public void setValorDanos(BigDecimal valorDanos) {
        this.valorDanos = valorDanos;
    }

    public boolean isDanosAprovados() {
        return danosAprovados;
    }

    public void setDanosAprovados(boolean danosAprovados) {
        this.danosAprovados = danosAprovados;
    }

    public BigDecimal getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }

    public Usuario getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(Usuario registradoPor) {
        this.registradoPor = registradoPor;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
