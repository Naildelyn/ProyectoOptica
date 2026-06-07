package visionmaster.enums;

public enum TipoGraduacion {
    MIOPIA("Miopía"),
    ASTIGMATISMO("Astigmatismo"),
    PRESBICIA("Presbicia"),
    NEUTRO("Sin graduación");

    private final String descripcion;

    TipoGraduacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() { return descripcion; }
}
