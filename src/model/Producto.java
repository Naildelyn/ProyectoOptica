package model;

public abstract class Producto {

    private String id;
    private String nombre;
    private double precioBase;

    public Producto(String id, String nombre, double precioBase) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    // Método abstracto obligatorio para las clases hijas
    public abstract double calcularPrecio();
}