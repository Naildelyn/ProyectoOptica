package visionmaster.model;

import visionmaster.enums.MaterialLente;
import visionmaster.enums.TipoGraduacion;

public class Armazon extends Producto {

    private String marca;
    private String color;
    private String estilo;
    private TipoGraduacion graduacion;
    private MaterialLente materialLente;

    public Armazon(int id, String nombre, double precioBase, String descripcion,
        String marca, String color, String estilo) {
        super(id, nombre, precioBase, descripcion);
        this.marca = marca;
        this.color = color;
        this.estilo = estilo;
        this.graduacion = TipoGraduacion.NEUTRO;
        this.materialLente = MaterialLente.RESINA;
    }

    // ----- Getters y Setters -----
    public String getMarca() { return marca; }
    public String getColor() { return color; }
    public String getEstilo() { return estilo; }
    public TipoGraduacion getGraduacion() { return graduacion; }
    public MaterialLente getMaterialLente() { return materialLente; }

    public void setGraduacion(TipoGraduacion graduacion) { this.graduacion = graduacion; }
    public void setMaterialLente(MaterialLente materialLente) { this.materialLente = materialLente; }

    // ----- Métodos -----
    @Override
    public double getPrecio() {
        return precio + materialLente.getCostoAdicional();
    }

    @Override
    public String getDetalleCompleto() {
        return String.format(
            "Armazón: %s (%s)\n  Marca: %s | Color: %s | Estilo: %s\n" +
            "  Graduación: %s | Material: %s\n  Precio base: $%.2f | Total: $%.2f",
            nombre, descripcion, marca, color, estilo,
            graduacion.getDescripcion(), materialLente.getDescripcion(),
            precio, getPrecio()
        );
    }

    @Override
    public String toString() {
        return String.format("[%d] %s – %s | %s | Precio: $%.2f",
            id, nombre, marca, estilo, getPrecio());
    }
}
