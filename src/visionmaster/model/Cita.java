package visionmaster.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import visionmaster.enums.EstadoCita;


public abstract class Cita {

    protected String folio;
    protected LocalDateTime fechaHora;
    protected EstadoCita estado;
    protected Cliente cliente;

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Cita(String folio, LocalDateTime fechaHora, Cliente cliente) {
        this.folio = folio;
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.estado = EstadoCita.PENDIENTE;
    }

    // ----- Getters -----
    public String getFolio() { return folio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public EstadoCita getEstado() { return estado; }
    public Cliente getCliente() { return cliente; }
    public String getFechaFormateada() { return fechaHora.format(FMT); }

    // ----- Acciones de estado -----
    public void confirmar() { this.estado = EstadoCita.CONFIRMADA; }
    public void cancelar()  { this.estado = EstadoCita.CANCELADA; }

        /**
        * Tipo de cita (Examen, Entrega, etc.) definido por cada subtipo.
        */
    public abstract String getTipoCita();


    public abstract String getDetalleAdicional();

    @Override
    public String toString() {
        return String.format("[%s] %s – %s – Estado: %s",
            folio, getTipoCita(), getFechaFormateada(), estado);
    }
}
