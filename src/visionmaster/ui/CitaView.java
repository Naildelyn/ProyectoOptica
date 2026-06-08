package visionmaster.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import visionmaster.model.Cita;
import visionmaster.model.CitaEntrega;
import visionmaster.model.CitaExamen;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class CitaView {

    private final VBox view;
    private final MainView main;

    private final ToggleGroup tgTipoCita = new ToggleGroup();
    private final RadioButton rbExamen   = new RadioButton("Examen visual");
    private final RadioButton rbEntrega  = new RadioButton("Entrega / Recolección");

    private final DatePicker dpFecha   = new DatePicker(LocalDate.now().plusDays(3));
    private final ComboBox<String> cbHora = new ComboBox<>();

    // Panel examen
    private final VBox panelExamen = new VBox(10);
    private final ComboBox<String> cbTipoExamen = new ComboBox<>();
    private final Label lblCostoExamen = new Label("$0.00");

    // Panel entrega
    private final VBox panelEntrega = new VBox(10);
    private final TextField tfDireccion = new TextField();
    private final CheckBox chkEnvio     = new CheckBox("Envío a domicilio");

    private final Label lblError = new Label();

    public CitaView(MainView main) {
        this.main = main;
        view = buildView();
    }

    private VBox buildView() {
        VBox root = new VBox(18);
        root.getStyleClass().add("step-panel");
        root.setPadding(new Insets(32, 40, 32, 40));

        Label titulo = new Label("Agenda tu cita");
        titulo.getStyleClass().add("step-title");
        Label subtitulo = new Label("Elige el tipo de cita, fecha y hora");
        subtitulo.getStyleClass().add("step-subtitle");

        // ── Tipo de cita ──────────────────────────────────────────
        rbExamen.setToggleGroup(tgTipoCita);
        rbEntrega.setToggleGroup(tgTipoCita);
        rbExamen.setSelected(true);
        rbExamen.setOnAction(e -> togglePaneles(true));
        rbEntrega.setOnAction(e -> togglePaneles(false));

        HBox tipoBox = new HBox(24, rbExamen, rbEntrega);
        tipoBox.setAlignment(Pos.CENTER_LEFT);

        // ── Fecha y hora ──────────────────────────────────────────
        dpFecha.getStyleClass().add("form-input");
        dpFecha.setMaxWidth(200);

        cbHora.setItems(FXCollections.observableArrayList(
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "14:00", "14:30", "15:00", "15:30", "16:00",
            "16:30", "17:00", "17:30", "18:00"));
        cbHora.setValue("10:00");
        cbHora.setMaxWidth(140);
        cbHora.getStyleClass().add("form-input");

        GridPane fechaGrid = new GridPane();
        fechaGrid.setHgap(16);
        fechaGrid.setVgap(12);
        fechaGrid.setMaxWidth(500);
        agregarFila(fechaGrid, "Fecha *", dpFecha, 0);
        agregarFila(fechaGrid, "Hora *",  cbHora,  1);

        // ── Panel examen ──────────────────────────────────────────
        panelExamen.setVisible(true);
        panelExamen.setManaged(true);

        cbTipoExamen.setItems(FXCollections.observableArrayList(
            "Examen básico de la vista (Gratuito)",
            "Topografía corneal ($350)",
            "Examen de fondo de ojo ($500)",
            "Evaluación completa ($700)"));
        cbTipoExamen.setValue("Examen básico de la vista (Gratuito)");
        cbTipoExamen.setMaxWidth(380);
        cbTipoExamen.getStyleClass().add("form-input");
        cbTipoExamen.setOnAction(e -> actualizarCostoExamen());

        actualizarCostoExamen();

        GridPane examenGrid = new GridPane();
        examenGrid.setHgap(16);
        examenGrid.setVgap(10);
        examenGrid.setMaxWidth(500);
        agregarFila(examenGrid, "Tipo de examen:", cbTipoExamen, 0);
        Label lblCostoKey = new Label("Costo:");
        lblCostoKey.getStyleClass().add("form-label");
        lblCostoExamen.getStyleClass().add("price-val");
        examenGrid.add(lblCostoKey, 0, 1);
        examenGrid.add(lblCostoExamen, 1, 1);
        panelExamen.getChildren().add(examenGrid);

        // ── Panel entrega ─────────────────────────────────────────
        panelEntrega.setVisible(false);
        panelEntrega.setManaged(false);

        tfDireccion.setPromptText("Calle, número, colonia, ciudad…");
        tfDireccion.setMaxWidth(380);
        tfDireccion.getStyleClass().add("form-input");

        chkEnvio.setOnAction(e -> tfDireccion.setDisable(!chkEnvio.isSelected()));
        tfDireccion.setDisable(true);

        GridPane entregaGrid = new GridPane();
        entregaGrid.setHgap(16);
        entregaGrid.setVgap(10);
        entregaGrid.setMaxWidth(500);
        agregarFila(entregaGrid, "¿Envío?", chkEnvio, 0);
        agregarFila(entregaGrid, "Dirección:", tfDireccion, 1);
        panelEntrega.getChildren().add(entregaGrid);

        lblError.getStyleClass().add("lbl-error");
        lblError.setVisible(false);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox nav = main.buildNavButtons("← Anterior", "Siguiente →",
            () -> main.mostrarPaso(1), this::validarYAvanzar);

        root.getChildren().addAll(
            titulo, subtitulo,
            new Label("Tipo de cita:") {{ getStyleClass().add("form-label"); }},
            tipoBox, fechaGrid, panelExamen, panelEntrega,
            lblError, spacer, nav
        );
        return root;
    }

    private void togglePaneles(boolean examenActivo) {
        panelExamen.setVisible(examenActivo);
        panelExamen.setManaged(examenActivo);
        panelEntrega.setVisible(!examenActivo);
        panelEntrega.setManaged(!examenActivo);
    }

    private void actualizarCostoExamen() {
        String sel = cbTipoExamen.getValue();
        if (sel == null) return;
        if (sel.contains("Gratuito")) lblCostoExamen.setText("Gratuito");
        else if (sel.contains("350"))  lblCostoExamen.setText("$350.00");
        else if (sel.contains("500"))  lblCostoExamen.setText("$500.00");
        else                           lblCostoExamen.setText("$700.00");
    }

    private void validarYAvanzar() {
        LocalDate fecha = dpFecha.getValue();
        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            mostrarError("Selecciona una fecha válida (hoy o posterior).");
            return;
        }

        String horaStr = cbHora.getValue();
        LocalTime hora = LocalTime.parse(horaStr);
        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);
        String folio = Tienda.generarFolioCita();

        Cita cita;
        if (rbExamen.isSelected()) {
            String tipo = cbTipoExamen.getValue();
            double costo = 0.0;
            if (tipo.contains("350")) costo = 350.0;
            else if (tipo.contains("500")) costo = 500.0;
            else if (tipo.contains("700")) costo = 700.0;

            cita = new CitaExamen(folio, fechaHora, main.getClienteActual(), tipo, costo);
        } else {
            if (chkEnvio.isSelected() && tfDireccion.getText().trim().isEmpty()) {
                mostrarError("Ingresa la dirección de envío.");
                return;
            }
            String dir = chkEnvio.isSelected() ? tfDireccion.getText().trim() : "Sucursal central";
            cita = new CitaEntrega(folio, fechaHora, main.getClienteActual(), dir, chkEnvio.isSelected());
        }

        cita.confirmar();
        main.getOrdenActual().setCita(cita);
        main.getTienda().agendarCita(cita);
        main.mostrarPaso(3);
    }

    private void mostrarError(String msg) {
        lblError.setText("⚠  " + msg);
        lblError.setVisible(true);
    }

    private void agregarFila(GridPane g, String lbl, Control ctrl, int row) {
        Label l = new Label(lbl);
        l.getStyleClass().add("form-label");
        g.add(l, 0, row);
        g.add(ctrl, 1, row);
    }


    private void agregarFila(GridPane g, String lbl, CheckBox ctrl, int row) {
        Label l = new Label(lbl);
        l.getStyleClass().add("form-label");
        g.add(l, 0, row);
        g.add(ctrl, 1, row);
    }

    // Import estático para que compile (Tienda ya importada a través de main)
    private static class Tienda {
        static String generarFolioCita() {
            return visionmaster.model.Tienda.generarFolioCita();
        }
    }

    public VBox getView() { return view; }
}
