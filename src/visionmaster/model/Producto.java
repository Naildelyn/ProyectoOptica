package visionmaster.model;

public abstract class Producto {

    protected int id;
    protected String nombre;
    protected double precio;
    protected String descripcion;

    public Producto(int id, String nombre, double precio, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // ----- Getters y Setters -----
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getDescripcion() { return descripcion; }
    
    public abstract String getDetalleCompleto();

    @Override
    public String toString() {
        return String.format("[%d] %s - $%.2f", id, nombre, precio);
    }
}
