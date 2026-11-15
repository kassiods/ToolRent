package br.edu.toolrent.api.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Pagamento {

    private UUID id;
    private BigDecimal valor;
    private String forma;
    private OffsetDateTime dataPagamento;
    private Usuario registradoPor;

    public Pagamento() {
    }

    public Pagamento(UUID id, BigDecimal valor, String forma, OffsetDateTime dataPagamento,
                     Usuario registradoPor) {
        this.id = id;
        this.valor = valor;
        this.forma = forma;
        this.dataPagamento = dataPagamento;
        this.registradoPor = registradoPor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public OffsetDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(OffsetDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Usuario getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(Usuario registradoPor) {
        this.registradoPor = registradoPor;
    }
}
