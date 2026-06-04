package model;

public class CitaExamen extends Cita {

    private double costo;

    public CitaExamen(String fecha, String hora, double costo) {
        super(fecha, hora);
        this.costo = costo;
    }

    public double getCosto() {
        return costo;
    }
}
