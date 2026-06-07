package visionmaster.enums;

/**
 * Materiales disponibles para los lentes.
 */
public enum MaterialLente {
    POLICARBONATO("Policarbonato", 350.0),
    RESINA("Resina", 200.0),
    CRISTAL("Cristal", 500.0);

    private final String descripcion;
    private final double costoAdicional;

    MaterialLente(String descripcion, double costoAdicional) {
        this.descripcion = descripcion;
        this.costoAdicional = costoAdicional;
    }

    public String getDescripcion() { return descripcion; }
    public double getCostoAdicional() { return costoAdicional; }

    @Override
    public String toString() { return descripcion + " (+$" + costoAdicional + ")"; }
}
