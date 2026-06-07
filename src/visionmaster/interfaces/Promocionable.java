package visionmaster.interfaces;

/**
 * Contrato para clases que pueden recibir o calcular descuentos.
 */
public interface Promocionable {
    double calcularDescuento(double total);
}
