package model;

public class Armazon extends Producto {

    private String Modelo;
    private TipoGraduacion graduacion;
    private MaterialLente material;

    public Armazon(String id, String nombre, double precioBase, String modelo, TipoGraduacion graduacion, MaterialLente material) {

        super(id, nombre, precioBase);

        this.Modelo = modelo;
        this.graduacion = graduacion;
        this.material = material;
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase();
    }

    public String getModelo() {
        return Modelo;
    }

    public TipoGraduacion getGraduacion() {
        return graduacion;
    }

    public MaterialLente getMaterial() {
        return material;
    }
}



