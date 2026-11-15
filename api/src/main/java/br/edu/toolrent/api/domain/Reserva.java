package br.edu.toolrent.api.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Reserva {

    private UUID id;
    private Cliente cliente;
    private Ferramenta ferramenta;
    private int quantidade;
    private OffsetDateTime dataInicio;
    private OffsetDateTime dataFim;
    private EstadoReserva estado;
    private String justificativa;
    private Usuario decididoPor;
    private OffsetDateTime decididoEm;
    private OffsetDateTime criadoEm;

    public Reserva() {
    }

    public Reserva(UUID id, Cliente cliente, Ferramenta ferramenta, int quantidade,
                   OffsetDateTime dataInicio, OffsetDateTime dataFim, EstadoReserva estado,
                   String justificativa, Usuario decididoPor, OffsetDateTime decididoEm,
                   OffsetDateTime criadoEm) {
        this.id = id;
        this.cliente = cliente;
        this.ferramenta = ferramenta;
        this.quantidade = quantidade;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.estado = estado;
        this.justificativa = justificativa;
        this.decididoPor = decididoPor;
        this.decididoEm = decididoEm;
        this.criadoEm = criadoEm;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public OffsetDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(OffsetDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public OffsetDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(OffsetDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Usuario getDecididoPor() {
        return decididoPor;
    }

    public void setDecididoPor(Usuario decididoPor) {
        this.decididoPor = decididoPor;
    }

    public OffsetDateTime getDecididoEm() {
        return decididoEm;
    }

    public void setDecididoEm(OffsetDateTime decididoEm) {
        this.decididoEm = decididoEm;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
}
