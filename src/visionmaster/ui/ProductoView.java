package visionmaster.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import visionmaster.enums.MaterialLente;
import visionmaster.enums.TipoGraduacion;
import visionmaster.model.Armazon;

import java.util.List;

//Pantalla 2 - Configuración del producto
public class ProductoView {

    private final VBox view;
    private final MainView main;

    private final ComboBox<Armazon> cbArmazon           = new ComboBox<>();
    private final ComboBox<TipoGraduacion> cbGraduacion = new ComboBox<>();
    private final ComboBox<MaterialLente> cbMaterial     = new ComboBox<>();
    private final Label lblPrecioBase   = new Label("$0.00");
    private final Label lblPrecioMat    = new Label("+$0.00");
    private final Label lblPrecioTotal  = new Label("$0.00");
    private final Label lblError        = new Label();

    private final Label lblDetalleMarca  = new Label("-");
    private final Label lblDetalleColor  = new Label("-");
    private final Label lblDetalleEstilo = new Label("-");

    public ProductoView(MainView main) {
        this.main = main;
        view = buildView();
    }

    private VBox buildView() {
        VBox root = new VBox(20);
        root.getStyleClass().add("step-panel");
        root.setPadding(new Insets(32, 40, 32, 40));

        Label titulo = new Label("Configuración del producto");
        titulo.getStyleClass().add("step-title");
        Label subtitulo = new Label("Selecciona el armazón y especifica la graduación");
        subtitulo.getStyleClass().add("step-subtitle");

        // ── Catálogo de armazones ordenado por precio ─────────────
        List<Armazon> armazones = main.getTienda().getCatalogoArmazones();
        cbArmazon.setItems(FXCollections.observableArrayList(armazones));
        cbArmazon.setMaxWidth(400);
        cbArmazon.setPromptText("— Selecciona un armazón —");
        cbArmazon.getStyleClass().add("form-input");
        cbArmazon.setOnAction(e -> onArmazonSeleccionado());

        // ── Enums en ComboBox ─────────────────────────────────────
        cbGraduacion.setItems(FXCollections.observableArrayList(TipoGraduacion.values()));
        cbGraduacion.setValue(TipoGraduacion.NEUTRO);
        cbGraduacion.setMaxWidth(280);
        cbGraduacion.getStyleClass().add("form-input");
        cbGraduacion.setOnAction(e -> actualizarPrecio());

        cbMaterial.setItems(FXCollections.observableArrayList(MaterialLente.values()));
        cbMaterial.setValue(MaterialLente.RESINA);
        cbMaterial.setMaxWidth(280);
        cbMaterial.getStyleClass().add("form-input");
        cbMaterial.setOnAction(e -> actualizarPrecio());

        // ── Formulario ────────────────────────────────────────────
        GridPane form = new GridPane();
        form.setHgap(16);
        form.setVgap(14);
        form.setMaxWidth(560);
        agregarFila(form, "Armazón *",            cbArmazon,    0);
        agregarFila(form, "Tipo de graduación *",  cbGraduacion, 1);
        agregarFila(form, "Material del lente *",  cbMaterial,   2);

        // ── Detalles del armazón ──────────────────────────────────
        VBox detalleBox = new VBox(6);
        detalleBox.getStyleClass().add("detail-card");
        detalleBox.setMaxWidth(480);
        detalleBox.setPadding(new Insets(14, 18, 14, 18));
        Label lblDetTitulo = new Label("Detalles del armazón seleccionado");
        lblDetTitulo.getStyleClass().add("card-title");
        GridPane detalleGrid = new GridPane();
        detalleGrid.setHgap(12);
        detalleGrid.setVgap(8);
        agregarDetalleFila(detalleGrid, "Marca:",  lblDetalleMarca,  0);
        agregarDetalleFila(detalleGrid, "Color:",  lblDetalleColor,  1);
        agregarDetalleFila(detalleGrid, "Estilo:", lblDetalleEstilo, 2);
        detalleBox.getChildren().addAll(lblDetTitulo, detalleGrid);

        // ── Resumen de precio ─────────────────────────────────────
        GridPane precioGrid = new GridPane();
        precioGrid.setHgap(12);
        precioGrid.setVgap(8);
        precioGrid.getStyleClass().add("price-box");
        precioGrid.setPadding(new Insets(12, 18, 12, 18));
        precioGrid.setMaxWidth(320);
        agregarPrecioFila(precioGrid, "Precio armazón:", lblPrecioBase,  0);
        agregarPrecioFila(precioGrid, "Material lente:", lblPrecioMat,   1);
        agregarPrecioFila(precioGrid, "Total armazón:",  lblPrecioTotal, 2);
        lblPrecioTotal.getStyleClass().add("price-total");

        lblError.getStyleClass().add("lbl-error");
        lblError.setVisible(false);

        // ── Botones de navegación ─────────────────────────────────
        HBox nav = main.buildNavButtons("← Anterior", "Siguiente →",
            () -> main.mostrarPaso(0), this::validarYAvanzar);

        root.getChildren().addAll(
            titulo, subtitulo, form,
            detalleBox, precioGrid, lblError, nav
        );
        return root;
    }

    private void onArmazonSeleccionado() {
        Armazon a = cbArmazon.getValue();
        if (a == null) return;
        lblDetalleMarca.setText(a.getMarca());
        lblDetalleColor.setText(a.getColor());
        lblDetalleEstilo.setText(a.getEstilo());
        actualizarPrecio();
    }

    private void actualizarPrecio() {
        Armazon a = cbArmazon.getValue();
        if (a == null) return;
        MaterialLente mat = cbMaterial.getValue();
        double base  = a.getPrecio() - a.getMaterialLente().getCostoAdicional();
        double extra = mat != null ? mat.getCostoAdicional() : 0;
        lblPrecioBase.setText(String.format("$%.2f", base));
        lblPrecioMat.setText(String.format("+$%.2f", extra));
        lblPrecioTotal.setText(String.format("$%.2f", base + extra));
    }

    private void validarYAvanzar() {
        if (cbArmazon.getValue() == null) {
            lblError.setText("⚠  Por favor selecciona un armazón.");
            lblError.setVisible(true);
            return;
        }
        Armazon armazon = cbArmazon.getValue();
        armazon.setGraduacion(cbGraduacion.getValue());
        armazon.setMaterialLente(cbMaterial.getValue());
        main.getOrdenActual().agregarProducto(armazon);
        main.mostrarPaso(2);
    }

    private void agregarFila(GridPane g, String lbl, Control ctrl, int row) {
        Label l = new Label(lbl);
        l.getStyleClass().add("form-label");
        g.add(l, 0, row);
        g.add(ctrl, 1, row);
    }

    private void agregarDetalleFila(GridPane g, String lbl, Label val, int row) {
        Label l = new Label(lbl);
        l.getStyleClass().add("detail-key");
        val.getStyleClass().add("detail-val");
        g.add(l, 0, row);
        g.add(val, 1, row);
    }

    private void agregarPrecioFila(GridPane g, String lbl, Label val, int row) {
        Label l = new Label(lbl);
        l.getStyleClass().add("price-key");
        g.add(l, 0, row);
        g.add(val, 1, row);
    }

    public VBox getView() { return view; }
}
