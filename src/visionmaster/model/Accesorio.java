package visionmaster.model;

public class Accesorio extends Producto {

    private String tipo;
    private boolean compatible;

    public Accesorio(int id, String nombre, double precio, String descripcion,
            String tipo, boolean compatible) {
        super(id, nombre, precio, descripcion);
        this.tipo = tipo;
        this.compatible = compatible;
    }

    public String getTipo() { return tipo; }
    public boolean isCompatible() { return compatible; }

    @Override
    public String getDetalleCompleto() {
        return String.format(
            "Accesorio: %s\n  Tipo: %s | Descripción: %s\n  Precio: $%.2f",
            nombre, tipo, descripcion, precio
        );
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - $%.2f", id, nombre, tipo, precio);
    }
}
