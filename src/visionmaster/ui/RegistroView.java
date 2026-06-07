package visionmaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import visionmaster.model.Cliente;
import visionmaster.model.OrdenCompra;

/**
 * Pantalla 1: Registro del cliente.
 */
public class RegistroView {

    private final VBox view;
    private final MainView main;

    private final TextField tfNombre   = new TextField();
    private final TextField tfTelefono = new TextField();
    private final TextField tfCorreo   = new TextField();
    private final Label lblError       = new Label();

    public RegistroView(MainView main) {
        this.main = main;
        view = buildView();
    }

    private VBox buildView() {
        VBox root = new VBox(20);
        root.getStyleClass().add("step-panel");
        root.setPadding(new Insets(32, 40, 32, 40));

        // Ttulo
        Label titulo = new Label("Registro del cliente");
        titulo.getStyleClass().add("step-title");
        Label subtitulo = new Label("Ingresa los datos del cliente para comenzar");
        subtitulo.getStyleClass().add("step-subtitle");

        // Formulario
        GridPane form = new GridPane();
        form.setHgap(16);
        form.setVgap(14);
        form.setMaxWidth(480);

        tfNombre.setPromptText("Ej. Mara Garca Lpez");
        tfTelefono.setPromptText("Ej. 5512345678");
        tfCorreo.setPromptText("Ej. maria@correo.com");

        agregarCampo(form, "Nombre completo *", tfNombre, 0);
        agregarCampo(form, "Telfono *",        tfTelefono, 1);
        agregarCampo(form, "Correo electrnico *", tfCorreo, 2);

        lblError.getStyleClass().add("lbl-error");
        lblError.setVisible(false);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        HBox nav = main.buildNavButtons(null, "Siguiente ", null, this::validarYAvanzar);

        root.getChildren().addAll(titulo, subtitulo, form, lblError, spacer, nav);
        return root;
    }

    private void agregarCampo(GridPane g, String label, TextField tf, int row) {
        Label lbl = new Label(label);
        lbl.getStyleClass().add("form-label");
        tf.getStyleClass().add("form-input");
        tf.setMaxWidth(340);
        g.add(lbl, 0, row);
        g.add(tf,  1, row);
    }

    private void validarYAvanzar() {
        String nombre   = tfNombre.getText().trim();
        String telefono = tfTelefono.getText().trim();
        String correo   = tfCorreo.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            mostrarError("Por favor, completa todos los campos obligatorios.");
            return;
        }
        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            mostrarError("El correo electrnico no tiene un formato vlido.");
            return;
        }
        if (!telefono.matches("\\d{10}")) {
            mostrarError("El telfono debe tener exactamente 10 dgitos.");
            return;
        }

        // Crear cliente y orden
        Cliente cliente = new Cliente(nombre, telefono, correo);
        main.getTienda().registrarCliente(cliente);
        main.setClienteActual(cliente);
        main.setOrdenActual(new OrdenCompra(cliente));

        main.mostrarPaso(1);
    }

    private void mostrarError(String msg) {
        lblError.setText(" " + msg);
        lblError.setVisible(true);
    }

    public VBox getView() { return view; }
}
