# VisionMaster – Sistema de Gestión para Óptica

## Descripción
Aplicación de escritorio en **Java + JavaFX** que simula el proceso completo de cotización,
personalización y compra de lentes en una óptica.

## Flujo de la aplicación (5 pasos)
1. **Registro del cliente** – captura nombre, teléfono y correo
2. **Configuración del producto** – catálogo de armazones (ordenado por precio), graduación (enum) y material (enum)
3. **Agenda de cita** – examen visual o entrega, con fecha/hora y estado (enum)
4. **Módulo de pago** – carrito en tiempo real, accesorios adicionales y cupón automático ≥$4,000
5. **Resumen y descarga** – ticket completo exportado a `ticket_FOLIO.txt`

## Requisitos técnicos implementados
| Concepto POO | Ubicación en el código |
|---|---|
| **Herencia** | `Producto` ← `Armazon`, `Accesorio`; `Cita` ← `CitaExamen`, `CitaEntrega` |
| **Interfaces** | `Descargable`, `Promocionable` → implementadas en `OrdenCompra` |
| **Enums** | `TipoGraduacion`, `MaterialLente`, `EstadoCita` |
| **Static** | `totalVentasGlobal`, `folioActual` en `Tienda`; `contadorId` en `Cliente` |
| **Colecciones** | `ArrayList<Producto>`, `ArrayList<Cita>`, `HashMap<Integer,Cliente>`, `List<OrdenCompra>` |

## Compilación

### Prerequisitos
- **JDK 17+** (recomendado JDK 21)
- **JavaFX SDK 21** – descárgalo desde: https://gluonhq.com/products/javafx/

### Pasos

```bash
# 1. Coloca el SDK de JavaFX en una carpeta, p. ej. C:\javafx-sdk-21\lib
# 2. Desde la raíz del proyecto:

# Compilar (Windows – ajusta la ruta a JavaFX)
javac --module-path "C:\javafx-sdk-21\lib" \
      --add-modules javafx.controls,javafx.fxml \
      -d out \
      src/module-info.java \
      src/visionmaster/**/*.java \
      src/visionmaster/*.java

# Copiar recursos CSS
xcopy src\visionmaster\styles out\visionmaster\styles /E /I

# Ejecutar
java --module-path "C:\javafx-sdk-21\lib" \
     --add-modules javafx.controls,javafx.fxml \
     -cp out visionmaster.App
```

### Con IntelliJ IDEA (recomendado)
1. Abre el proyecto como proyecto Java estándar.
2. Ve a `File → Project Structure → Libraries` y agrega el `lib` de JavaFX.
3. En `Run/Debug Configurations` del `App.java`, agrega en VM Options:
   ```
   --module-path /ruta/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml
   ```
4. Ejecuta `App.java`.

### Con Maven (alternativo)
Puedes migrar a Maven añadiendo la dependencia `org.openjfx:javafx-controls:21`
y el plugin `javafx-maven-plugin`.

## Estructura de archivos
```
VisionMaster/
└── src/
    ├── module-info.java
    └── visionmaster/
        ├── App.java                        ← Punto de entrada
        ├── enums/
        │   ├── TipoGraduacion.java
        │   ├── MaterialLente.java
        │   └── EstadoCita.java
        ├── interfaces/
        │   ├── Descargable.java
        │   └── Promocionable.java
        ├── model/
        │   ├── Producto.java               ← Clase abstracta base
        │   ├── Armazon.java
        │   ├── Accesorio.java
        │   ├── Cita.java                   ← Clase abstracta base
        │   ├── CitaExamen.java
        │   ├── CitaEntrega.java
        │   ├── Cliente.java
        │   ├── OrdenCompra.java
        │   └── Tienda.java
        ├── ui/
        │   ├── MainView.java               ← Controlador de pantallas
        │   ├── RegistroView.java           ← Paso 1
        │   ├── ProductoView.java           ← Paso 2
        │   ├── CitaView.java               ← Paso 3
        │   ├── PagoView.java               ← Paso 4
        │   └── ResumenView.java            ← Paso 5
        └── styles/
            └── app.css
```
