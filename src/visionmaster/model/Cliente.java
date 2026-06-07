package visionmaster.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private static int contadorId = 1;
    private int id;
    private String nombre;
    private String telefono;
    private String correo;
    private List<OrdenCompra> historialCompras;

    public Cliente(String nombre, String telefono, String correo) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.historialCompras = new ArrayList<>();
    }

    // ----- Getters -----
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public List<OrdenCompra> getHistorialCompras() { return historialCompras; }

    // ----- Setters -----
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }

    /**
     * Agrega una orden al historial del cliente.
     */
    public void agregarOrden(OrdenCompra orden) {
        historialCompras.add(orden);
    }

    public static void resetContador() { contadorId = 1; }

    @Override
    public String toString() {
        return String.format("Cliente #%d – %s | Tel: %s | Correo: %s",
            id, nombre, telefono, correo);
    }
}
