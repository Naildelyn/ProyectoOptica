package visionmaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import visionmaster.model.Accesorio;
import visionmaster.model.OrdenCompra;
import visionmaster.model.Producto;
import visionmaster.model.Tienda;

/**
 * Pantalla 4: Mdulo de pago y aplicacin automtica de cupones.
 */
public class PagoView {

    private final VBox view;
    private final MainView main;

    private final Label lblSubtotal   = new Label();
    private final Label lblDescuento  = new Label();
    private final Label lblTotal      = new Label();
    private final Label lblCuponInfo  = new Label();
    private final VBox listaProductos = new VBox(8);

    public PagoView(MainView main) {
        this.main = main;
        view = buildView();
    }

    private VBox buildView() {
        VBox root = new VBox(20);
        root.getStyleClass().add("step-panel");
        root.setPadding(new Insets(32, 40, 32, 40));

        Label titulo = new Label("Resumen y pago");
        titulo.getStyleClass().add("step-title");
        Label subtitulo = new Label("Revisa tu pedido y confirma el pago");
        subtitulo.getStyleClass().add("step-subtitle");

        //  Lista de productos 
        Label lblProdTitulo = new Label("Productos en tu carrito");
        lblProdTitulo.getStyleClass().add("section-title");

        actualizarListaProductos();

        //  Seccin de accesorios adicionales 
        Label lblAccTitulo = new Label("¿Deseas agregar algún accesorio?");
        lblAccTitulo.getStyleClass().add("section-title");

        HBox accesoriosBox = new HBox(10);
        accesoriosBox.setAlignment(Pos.CENTER_LEFT);
        main.getTienda().getInventario().stream()
            .filter(p -> p instanceof Accesorio)
            .forEach(p -> {
                Button btn = new Button("+ " + p.getNombre() + "  $" + p.getPrecio());
                btn.getStyleClass().add("btn-accesorio");
                btn.setOnAction(e -> agregarAccesorio((Accesorio) p, btn));
                accesoriosBox.getChildren().add(btn);
            });

        //  Panel de totales 
        VBox totalesBox = new VBox(8);
        totalesBox.getStyleClass().add("totales-box");
        totalesBox.setPadding(new Insets(16, 20, 16, 20));
        totalesBox.setMaxWidth(360);

        lblCuponInfo.getStyleClass().add("cupon-badge");
        lblCuponInfo.setVisible(false);
        lblCuponInfo.setManaged(false);

        GridPane totalesGrid = new GridPane();
        totalesGrid.setHgap(24);
        totalesGrid.setVgap(10);

        agregarFilaTotales(totalesGrid, "Subtotal:",  lblSubtotal, 0);
        agregarFilaTotales(totalesGrid, "Cupn :", lblDescuento, 1);
        agregarFilaTotales(totalesGrid, "TOTAL:",     lblTotal,    2);
        lblTotal.getStyleClass().add("price-total");

        totalesBox.getChildren().addAll(lblCuponInfo, totalesGrid);
        actualizarTotales();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox nav = main.buildNavButtons(" Anterior", "Confirmar pedido ",
            () -> main.mostrarPaso(2), this::confirmarYAvanzar);

        root.getChildren().addAll(
            titulo, subtitulo,
            lblProdTitulo, listaProductos,
            lblAccTitulo, accesoriosBox,
            totalesBox, spacer, nav
        );
        return root;
    }

    private void actualizarListaProductos() {
        listaProductos.getChildren().clear();
        for (Producto p : main.getOrdenActual().getProductos()) {
            HBox row = new HBox();
            row.getStyleClass().add("producto-row");
            row.setPadding(new Insets(8, 12, 8, 12));
            row.setAlignment(Pos.CENTER_LEFT);

            Label nombre = new Label(p.getNombre());
            nombre.getStyleClass().add("prod-nombre");
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            Label precio = new Label(String.format("$%.2f", p.getPrecio()));
            precio.getStyleClass().add("prod-precio");

            Button btnEliminar = new Button("");
            btnEliminar.getStyleClass().add("btn-remove");
            btnEliminar.setOnAction(e -> {
                main.getOrdenActual().eliminarProducto(p);
                actualizarListaProductos();
                actualizarTotales();
            });

            row.getChildren().addAll(nombre, sp, precio, btnEliminar);
            listaProductos.getChildren().add(row);
        }
    }

    private void agregarAccesorio(Accesorio acc, Button btn) {
        main.getOrdenActual().agregarProducto(acc);
        btn.setDisable(true);
        btn.setText("" + acc.getNombre());
        actualizarListaProductos();
        actualizarTotales();
    }

    private void actualizarTotales() {
        OrdenCompra orden = main.getOrdenActual();
        lblSubtotal.setText(String.format("$%.2f", orden.getSubtotal()));
        lblTotal.setText(String.format("$%.2f", orden.getTotalNeto()));

        if (orden.isCuponAplicado()) {
            lblDescuento.setText(String.format("-$%.2f", orden.getDescuentoAplicado()));
            lblDescuento.getStyleClass().add("descuento-activo");
            lblCuponInfo.setText(" Cupón aplicado! Tu compra supera $"
                + (int) OrdenCompra.getMontoMinimoCupon()
                + "  descuento automtico de $" + (int) OrdenCompra.getValorCupon());
            lblCuponInfo.setVisible(true);
            lblCuponInfo.setManaged(true);
        } else {
            lblDescuento.setText("$0.00");
            lblDescuento.getStyleClass().remove("descuento-activo");
            lblCuponInfo.setVisible(false);
            lblCuponInfo.setManaged(false);
        }
    }

    private void confirmarYAvanzar() {
        if (main.getOrdenActual().getProductos().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                "Debes tener al menos un producto en el carrito.",
                ButtonType.OK).showAndWait();
            return;
        }
        main.getTienda().procesarVenta(main.getOrdenActual());
        main.mostrarPaso(4);
    }

    private void agregarFilaTotales(GridPane g, String key, Label val, int row) {
        Label k = new Label(key);
        k.getStyleClass().add("price-key");
        g.add(k, 0, row);
        g.add(val, 1, row);
    }

    public VBox getView() { return view; }
}
