package visionmaster.model;

import java.time.LocalDateTime;

class CitaEntrega extends Cita {

    private String direccion;
    private boolean requiereEnvio;

    public CitaEntrega(String folio, LocalDateTime fechaHora, Cliente cliente,
                        String direccion, boolean requiereEnvio) {
        super(folio, fechaHora, cliente);
        this.direccion = direccion;
        this.requiereEnvio = requiereEnvio;
    }

    public String getDireccion() { return direccion; }
    public boolean isRequiereEnvio() { return requiereEnvio; }

    @Override
    public String getTipoCita() { return "Cita de entrega"; }

    @Override
    public String getDetalleAdicional() {
        if (requiereEnvio) {
            return "Envo a domicilio: " + direccion;
        } else {
            return "Recoleccin en sucursal";
        }
    }
}
