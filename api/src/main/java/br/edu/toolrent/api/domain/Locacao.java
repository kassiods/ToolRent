package br.edu.toolrent.api.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Locacao {

    private UUID id;
    private Cliente cliente;

    /** Nula quando a locação é formalizada sem reserva. */
    private Reserva reserva;
    private OffsetDateTime dataInicio;
    private OffsetDateTime dataPrevistaDevolucao;
    private OffsetDateTime dataRetirada;
    private EstadoLocacao estado;
    private BigDecimal valorPrevisto;
    private BigDecimal valorTotal;
    private Usuario criadoPor;
    private OffsetDateTime criadoEm;
    private List<ItemLocacao> itens = new ArrayList<>();

    /** Registro único da devolução integral, quando realizada. */
    private Devolucao devolucao;
    private List<Pagamento> pagamentos = new ArrayList<>();

    public Locacao() {
    }

    public Locacao(UUID id, Cliente cliente, Reserva reserva, OffsetDateTime dataInicio,
                   OffsetDateTime dataPrevistaDevolucao, OffsetDateTime dataRetirada,
                   EstadoLocacao estado, BigDecimal valorPrevisto, BigDecimal valorTotal,
                   Usuario criadoPor, OffsetDateTime criadoEm, List<ItemLocacao> itens,
                   Devolucao devolucao, List<Pagamento> pagamentos) {
        this.id = id;
        this.cliente = cliente;
        this.reserva = reserva;
        this.dataInicio = dataInicio;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataRetirada = dataRetirada;
        this.estado = estado;
        this.valorPrevisto = valorPrevisto;
        this.valorTotal = valorTotal;
        this.criadoPor = criadoPor;
        this.criadoEm = criadoEm;
        this.itens = itens;
        this.devolucao = devolucao;
        this.pagamentos = pagamentos;
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

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public OffsetDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(OffsetDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public OffsetDateTime getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(OffsetDateTime dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public OffsetDateTime getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(OffsetDateTime dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public EstadoLocacao getEstado() {
        return estado;
    }

    public void setEstado(EstadoLocacao estado) {
        this.estado = estado;
    }

    public BigDecimal getValorPrevisto() {
        return valorPrevisto;
    }

    public void setValorPrevisto(BigDecimal valorPrevisto) {
        this.valorPrevisto = valorPrevisto;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Usuario getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(Usuario criadoPor) {
        this.criadoPor = criadoPor;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public List<ItemLocacao> getItens() {
        return itens;
    }

    public void setItens(List<ItemLocacao> itens) {
        this.itens = itens;
    }

    public Devolucao getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(Devolucao devolucao) {
        this.devolucao = devolucao;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
