package model;

public abstract class Cita {

    private String fecha;
    private String hora;
    private EstadoCita estado; // Uso de enum

    public Cita(String fecha, String hora) {
        this.fecha = fecha;
        this.hora = hora;
        this.estado = EstadoCita.PENDIENTE; // Estado inicial
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
}
