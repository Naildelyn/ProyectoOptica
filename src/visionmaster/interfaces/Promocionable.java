package visionmaster.interfaces;

/**
 * Contrato para clases que pueden recibir o calcular descuentos.
 */
public interface Promocionable {
    /**
     * Calcula el descuento aplicable sobre un monto dado.
     * @param total Monto antes del descuento.
     * @return Monto del descuento a aplicar.
     */
    double calcularDescuento(double total);
}
