package visionmaster.model;

import java.util.*;
import java.util.stream.Collectors;

public class Tienda {

    // ----- Atributos estáticos (globales) -----
    private static int totalVentasGlobal = 0;
    private static int folioContador    = 0;
    public  static final double MONTO_CUPON = 4000.0;
    public  static final double VALOR_CUPON = 400.0;

    // ----- Atributos de instancia -----
    private String nombreSucursal;
    private ArrayList<Producto> inventario;
    private ArrayList<Cita> citas;
    private HashMap<Integer, Cliente> clientes;

    public Tienda(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
        this.inventario = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.clientes = new HashMap<>();
        cargarInventarioInicial();
    }

    // ============================================================
    //  MÉTODOS ESTÁTICOS – reglas de negocio globales
    // ============================================================

    /** Valida si el monto califica para el cupón de descuento. */
    public static boolean calificaParaCupon(double monto) {
        return monto >= MONTO_CUPON;
    }

    /** Devuelve el siguiente folio de cita como texto formateado. */
    public static String generarFolioCita() {
        return "CIT-" + String.format("%04d", ++folioContador);
    }

    public static int getTotalVentasGlobal() { return totalVentasGlobal; }

    // ============================================================
    //  GESTIÓN DE CLIENTES
    // ============================================================

    public void registrarCliente(Cliente c) {
        clientes.put(c.getId(), c);
    }

    public Cliente buscarCliente(int id) {
        return clientes.get(id);
    }

    public Collection<Cliente> getTodosClientes() {
        return clientes.values();
    }

    // ============================================================
    //  GESTIÓN DE INVENTARIO
    // ============================================================

    public void agregarProducto(Producto p) { inventario.add(p); }

    public Producto buscarProducto(int id) {
        return inventario.stream()
            .filter(p -> p.getId() == id)
            .findFirst().orElse(null);
    }


    public List<Producto> getInventarioOrdenadoPorPrecio() {
        return inventario.stream()
            .sorted(Comparator.comparingDouble(Producto::getPrecio))
            .collect(Collectors.toList());
    }

    public List<Armazon> getCatalogoArmazones() {
        return inventario.stream()
            .filter(p -> p instanceof Armazon)
            .map(p -> (Armazon) p)
            .sorted(Comparator.comparingDouble(Producto::getPrecio))
            .collect(Collectors.toList());
    }

    public ArrayList<Producto> getInventario() { return inventario; }

    // ============================================================
    //  GESTIÓN DE CITAS
    // ============================================================

    public void agendarCita(Cita cita) { citas.add(cita); }

    public List<Cita> getCitasDeCliente(int clienteId) {
        return citas.stream()
            .filter(c -> c.getCliente().getId() == clienteId)
            .collect(Collectors.toList());
    }

    public ArrayList<Cita> getTodasCitas() { return citas; }

    // ============================================================
    //  PROCESAMIENTO DE VENTA
    // ============================================================

    /**
     * Procesa la venta final: registra la orden, actualiza historial
     * del cliente e incrementa el contador global.
     */
    public void procesarVenta(OrdenCompra orden) {
        orden.getCliente().agregarOrden(orden);
        totalVentasGlobal++;
    }

    public String getNombreSucursal() { return nombreSucursal; }

    // ============================================================
    //  INVENTARIO INICIAL DE DEMOSTRACIÓN
    // ============================================================

    private void cargarInventarioInicial() {
        // Armazones
        inventario.add(new Armazon(1, "Classic Pro", 1200.0,
            "Armazón clásico de metal", "Ray-Ban", "Negro", "Cuadrado"));
        inventario.add(new Armazon(2, "AeroFlex", 980.0,
            "Ligero y flexible", "Oakley", "Azul", "Deportivo"));
        inventario.add(new Armazon(3, "VintageRound", 1500.0,
            "Estilo retro redondeado", "Persol", "Carey", "Redondo"));
        inventario.add(new Armazon(4, "UrbanSlim", 750.0,
            "Minimalista y moderno", "Zara", "Gris", "Cuadrado"));
        inventario.add(new Armazon(5, "AviatorGold", 2200.0,
            "Armazón tipo aviador dorado", "Tom Ford", "Dorado", "Aviador"));
        inventario.add(new Armazon(6, "KidsStar", 450.0,
            "Resistente para niños", "OtticaKids", "Rojo", "Redondo"));

        // Accesorios
        inventario.add(new Accesorio(101, "Estuche Rígido Premium", 180.0,
            "Protección total", "Estuche", true));
        inventario.add(new Accesorio(102, "Paño Microfibra", 45.0,
            "Limpieza sin rayones", "Paño", true));
        inventario.add(new Accesorio(103, "Cadena Decorativa", 120.0,
            "Cadena antiderrapante", "Cadena", true));
        inventario.add(new Accesorio(104, "Spray Limpiador", 65.0,
            "Limpieza profesional 60 ml", "Limpiador", true));
    }
}
