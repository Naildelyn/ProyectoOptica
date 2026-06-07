package visionmaster.model;

import visionmaster.interfaces.Descargable;
import visionmaster.interfaces.Promocionable;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una orden de compra completa.
 * Implementa Descargable (genera ticket .txt) y Promocionable (aplica cupones).
 */
public class OrdenCompra implements Descargable, Promocionable {

    // ----- Constantes y atributos estticos -----
    private static final double MONTO_MINIMO_CUPON = 4000.0;
    private static final double VALOR_CUPON        = 400.0;
    private static int folioActual = 1000;  // Folio autoincremental

    // ----- Atributos de instancia -----
    private String folio;
    private Cliente cliente;
    private List<Producto> productos;    // Coleccin: ArrayList
    private Cita cita;
    private double subtotal;
    private double descuentoAplicado;
    private double totalNeto;
    private boolean cuponAplicado;
    private LocalDateTime fechaEmision;

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public OrdenCompra(Cliente cliente) {
        this.folio = "ORD-" + (++folioActual);
        this.cliente = cliente;
        this.productos = new ArrayList<>();
        this.fechaEmision = LocalDateTime.now();
        this.cuponAplicado = false;
        this.descuentoAplicado = 0.0;
    }

    // ----- Gestin de productos (coleccin en tiempo real) -----
    public void agregarProducto(Producto p) {
        productos.add(p);
        recalcular();
    }

    public void eliminarProducto(Producto p) {
        productos.remove(p);
        recalcular();
    }

    public void setCita(Cita cita) { this.cita = cita; }

    /** Recalcula subtotal, descuento y total en cada cambio. */
    private void recalcular() {
        subtotal = productos.stream().mapToDouble(Producto::getPrecio).sum();
        descuentoAplicado = calcularDescuento(subtotal);
        cuponAplicado = descuentoAplicado > 0;
        totalNeto = subtotal - descuentoAplicado;
    }

    // ----- Implementacin de Promocionable -----
    @Override
    public double calcularDescuento(double total) {
        // Lgica de cupn: si supera $4000, descuenta $400
        return total >= MONTO_MINIMO_CUPON ? VALOR_CUPON : 0.0;
    }

    // ----- Implementacin de Descargable -----
    @Override
    public void exportarAArchivo(String ruta) {
        String rutaFinal = ruta + "/ticket_" + folio + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaFinal))) {
            pw.println("=".repeat(52));
            pw.println("         PTICA VISIONMASTER");
            pw.println("         Comprobante de Compra");
            pw.println("=".repeat(52));
            pw.printf("Folio:        %s%n", folio);
            pw.printf("Fecha:        %s%n", fechaEmision.format(FMT));
            pw.println("-".repeat(52));
            pw.println("DATOS DEL CLIENTE");
            pw.printf("Nombre:       %s%n", cliente.getNombre());
            pw.printf("Telfono:     %s%n", cliente.getTelefono());
            pw.printf("Correo:       %s%n", cliente.getCorreo());
            pw.println("-".repeat(52));
            pw.println("PRODUCTOS ADQUIRIDOS");
            for (Producto p : productos) {
                pw.println(p.getDetalleCompleto());
                pw.printf("   Precio: $%.2f%n", p.getPrecio());
                pw.println();
            }
            pw.println("-".repeat(52));
            if (cita != null) {
                pw.println("CITA AGENDADA");
                pw.printf("Tipo:         %s%n", cita.getTipoCita());
                pw.printf("Fecha/Hora:   %s%n", cita.getFechaFormateada());
                pw.printf("Estado:       %s%n", cita.getEstado());
                pw.printf("Detalle:      %s%n", cita.getDetalleAdicional());
                pw.println("-".repeat(52));
            }
            pw.println("RESUMEN DE PAGO");
            pw.printf("Subtotal:     $%.2f%n", subtotal);
            if (cuponAplicado) {
                pw.printf("Cupn regalo: -$%.2f (compra  $%.0f)%n",
                    descuentoAplicado, MONTO_MINIMO_CUPON);
            }
            pw.printf("TOTAL:        $%.2f%n", totalNeto);
            pw.println("=".repeat(52));
            pw.println("  Gracias por su preferencia, VisionMaster!");
            pw.println("=".repeat(52));
        } catch (IOException e) {
            System.err.println("Error al generar el comprobante: " + e.getMessage());
        }
    }

    // ----- Getters -----
    public String getFolio()            { return folio; }
    public Cliente getCliente()         { return cliente; }
    public List<Producto> getProductos(){ return productos; }
    public Cita getCita()               { return cita; }
    public double getSubtotal()         { return subtotal; }
    public double getDescuentoAplicado(){ return descuentoAplicado; }
    public double getTotalNeto()        { return totalNeto; }
    public boolean isCuponAplicado()    { return cuponAplicado; }
    public LocalDateTime getFechaEmision(){ return fechaEmision; }
    public static double getMontoMinimoCupon() { return MONTO_MINIMO_CUPON; }
    public static double getValorCupon()       { return VALOR_CUPON; }

    @Override
    public String toString() {
        return String.format("Orden %s | Cliente: %s | Total: $%.2f",
            folio, cliente.getNombre(), totalNeto);
    }
}
