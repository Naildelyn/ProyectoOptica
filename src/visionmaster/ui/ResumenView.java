package visionmaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import visionmaster.model.*;

import java.io.File;
import java.time.format.DateTimeFormatter;


public class ResumenView {

    private final VBox view;
    private final MainView main;
    private final Stage stage;

    public ResumenView(MainView main, Stage stage) {
        this.main  = main;
        this.stage = stage;
        view = buildView();
    }

    private VBox buildView() {
        VBox root = new VBox(18);
        root.getStyleClass().add("step-panel");
        root.setPadding(new Insets(32, 40, 32, 40));

        OrdenCompra orden = main.getOrdenActual();
        Cliente cliente   = main.getClienteActual();

        // ── Cabecera de éxito ──────────────────────────────────────
        Label iconExito = new Label("");
        iconExito.setStyle("-fx-font-size: 40px;");
        Label titulo = new Label("¡Pedido confirmado!");
        titulo.getStyleClass().add("step-title");
        Label folio = new Label("Folio: " + orden.getFolio());
        folio.getStyleClass().add("step-subtitle");
        folio.setStyle("-fx-font-weight: bold;");

        VBox headerBox = new VBox(6, iconExito, titulo, folio);
        headerBox.setAlignment(Pos.CENTER);

        // ── Datos del cliente ──────────────────────────────────────
        TitledPane panelCliente = crearPanel("Cliente",
            buildGrid(new String[][]{
                {"Nombre:",   cliente.getNombre()},
                {"Teléfono:", cliente.getTelefono()},
                {"Correo:",   cliente.getCorreo()},
            }));

        // ── Productos ──────────────────────────────────────────────
        VBox prodList = new VBox(6);
        for (Producto p : orden.getProductos()) {
            HBox row = new HBox();
            Label lNom = new Label(p.getNombre());
            lNom.getStyleClass().add("prod-nombre");
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            Label lPrecio = new Label(String.format("$%.2f", p.getPrecio()));
            lPrecio.getStyleClass().add("prod-precio");
            row.getChildren().addAll(lNom, sp, lPrecio);
            prodList.getChildren().add(row);
        }
        TitledPane panelProductos = crearPanel("Productos", prodList);

        // ── Cita ──────────────────────────────────────────────────
        TitledPane panelCita = null;
        if (orden.getCita() != null) {
            Cita c = orden.getCita();
            panelCita = crearPanel("Cita agendada",
                buildGrid(new String[][]{
                    {"Tipo:",      c.getTipoCita()},
                    {"Fecha:",     c.getFechaFormateada()},
                    {"Estado:",    c.getEstado().toString()},
                    {"Detalle:",   c.getDetalleAdicional()},
                }));
        }

        // ── Totales ────────────────────────────────────────────────
        String[][] totalesData = orden.isCuponAplicado()
            ? new String[][]{
                {"Subtotal:",  String.format("$%.2f", orden.getSubtotal())},
                {"Cupón aplicado:", String.format("-$%.2f", orden.getDescuentoAplicado())},
                {"TOTAL:",     String.format("$%.2f", orden.getTotalNeto())}}
            : new String[][]{
                {"Subtotal:",  String.format("$%.2f", orden.getSubtotal())},
                {"TOTAL:",     String.format("$%.2f", orden.getTotalNeto())}};

        TitledPane panelTotales = crearPanel("Resumen de pago", buildGrid(totalesData));

        // ── Botones de acción ──────────────────────────────────────
        Button btnDescargar = new Button(" Descargar comprobante (.txt)");
        btnDescargar.getStyleClass().add("btn-primary");
        btnDescargar.setOnAction(e -> descargarComprobante());

        Button btnNuevaCompra = new Button(" Nueva compra");
        btnNuevaCompra.getStyleClass().add("btn-secondary");
        btnNuevaCompra.setOnAction(e -> main.mostrarPaso(0));

        Label lblVentas = new Label("Total de ventas en esta sesión: "
            + visionmaster.model.Tienda.getTotalVentasGlobal());
        lblVentas.getStyleClass().add("step-subtitle");

        HBox btnBox = new HBox(12, btnDescargar, btnNuevaCompra);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().addAll(headerBox, panelCliente, panelProductos);
        if (panelCita != null) root.getChildren().add(panelCita);
        root.getChildren().addAll(panelTotales, btnBox, lblVentas);

        return root;
    }

    private void descargarComprobante() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Selecciona la carpeta de destino");
        File carpeta = dc.showDialog(stage);

        if (carpeta != null) {
            main.getOrdenActual().exportarAArchivo(carpeta.getAbsolutePath());
            Alert ok = new Alert(Alert.AlertType.INFORMATION,
                "Comprobante guardado en:\n" + carpeta.getAbsolutePath()
                    + "/ticket_" + main.getOrdenActual().getFolio() + ".txt",
                ButtonType.OK);
            ok.setHeaderText("Comprobante generado con éxito");
            ok.showAndWait();
        }
    }

    private TitledPane crearPanel(String titulo, Region contenido) {
        contenido.setPadding(new Insets(10, 14, 10, 14));
        TitledPane tp = new TitledPane(titulo, contenido);
        tp.setCollapsible(true);
        tp.setExpanded(true);
        tp.getStyleClass().add("resumen-panel");
        return tp;
    }

    private GridPane buildGrid(String[][] filas) {
        GridPane g = new GridPane();
        g.setHgap(16);
        g.setVgap(8);
        for (int i = 0; i < filas.length; i++) {
            Label k = new Label(filas[i][0]);
            k.getStyleClass().add("detail-key");
            Label v = new Label(filas[i][1]);
            v.getStyleClass().add("detail-val");
            g.add(k, 0, i);
            g.add(v, 1, i);
        }
        return g;
    }

    public VBox getView() { return view; }
}
