package visionmaster.model;

import java.time.LocalDateTime;

/**
 * Cita de examen visual, puede ser gratuita o con costo.
 * Hereda de Cita.
 */
public class CitaExamen extends Cita {

    private double costo;
    private String tipoExamen;

    public CitaExamen(String folio, LocalDateTime fechaHora, Cliente cliente,
                    String tipoExamen, double costo) {
        super(folio, fechaHora, cliente);
        this.tipoExamen = tipoExamen;
        this.costo = costo;
    }

    public boolean esGratuita() { return costo == 0.0; }
    public double getCosto() { return costo; }
    public String getTipoExamen() { return tipoExamen; }

    @Override
    public String getTipoCita() { return "Cita de examen"; }

    @Override
    public String getDetalleAdicional() {
        return String.format("Tipo: %s | Costo: %s",
            tipoExamen, esGratuita() ? "Gratuito" : "$" + costo);
    }
}
