package br.edu.toolrent.api.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemLocacao {

    private UUID id;
    private Ferramenta ferramenta;
    private int quantidade;
    private BigDecimal tarifaRegistrada;
    private String condicaoRetirada;

    public ItemLocacao() {
    }

    public ItemLocacao(UUID id, Ferramenta ferramenta, int quantidade,
                       BigDecimal tarifaRegistrada, String condicaoRetirada) {
        this.id = id;
        this.ferramenta = ferramenta;
        this.quantidade = quantidade;
        this.tarifaRegistrada = tarifaRegistrada;
        this.condicaoRetirada = condicaoRetirada;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Ferramenta getFerramenta() {
        return ferramenta;
    }

    public void setFerramenta(Ferramenta ferramenta) {
        this.ferramenta = ferramenta;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getTarifaRegistrada() {
        return tarifaRegistrada;
    }

    public void setTarifaRegistrada(BigDecimal tarifaRegistrada) {
        this.tarifaRegistrada = tarifaRegistrada;
    }

    public String getCondicaoRetirada() {
        return condicaoRetirada;
    }

    public void setCondicaoRetirada(String condicaoRetirada) {
        this.condicaoRetirada = condicaoRetirada;
    }
}
