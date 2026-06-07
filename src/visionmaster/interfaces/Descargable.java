package visionmaster.interfaces;

/**
 * Contrato para clases cuya informacin puede exportarse a archivo.
 */
public interface Descargable {
    /**
     * Exporta la informacin del objeto a un archivo de texto.
     * @param ruta Ruta donde se crear el archivo.
     */
    void exportarAArchivo(String ruta);
}
