package br.edu.toolrent.api.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Manutencao {

    private UUID id;
    private String motivo;
    private String descricao;
    private EstadoManutencao estado;
    private String resultado;
    private OffsetDateTime abertaEm;
    private OffsetDateTime concluidaEm;

    public Manutencao() {
    }

    public Manutencao(UUID id, String motivo, String descricao,
                      EstadoManutencao estado, String resultado,
                      OffsetDateTime abertaEm, OffsetDateTime concluidaEm) {
        this.id = id;
        this.motivo = motivo;
        this.descricao = descricao;
        this.estado = estado;
        this.resultado = resultado;
        this.abertaEm = abertaEm;
        this.concluidaEm = concluidaEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EstadoManutencao getEstado() {
        return estado;
    }

    public void setEstado(EstadoManutencao estado) {
        this.estado = estado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public OffsetDateTime getAbertaEm() {
        return abertaEm;
    }

    public void setAbertaEm(OffsetDateTime abertaEm) {
        this.abertaEm = abertaEm;
    }

    public OffsetDateTime getConcluidaEm() {
        return concluidaEm;
    }

    public void setConcluidaEm(OffsetDateTime concluidaEm) {
        this.concluidaEm = concluidaEm;
    }
}
