package visionmaster.enums;

/**
 * Tipos de graduacin disponibles para los lentes.
 */
public enum TipoGraduacion {
    MIOPIA("Miopa"),
    ASTIGMATISMO("Astigmatismo"),
    PRESBICIA("Presbicia"),
    NEUTRO("Sin graduacin");

    private final String descripcion;

    TipoGraduacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() { return descripcion; }
}
