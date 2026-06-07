package visionmaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import visionmaster.model.*;

public class MainView {

    private final BorderPane root;
    private final Stage stage;

    
    private final Tienda tienda;
    private Cliente clienteActual;
    private OrdenCompra ordenActual;

    // Indicadores de pasos
    private final Label[] stepLabels = new Label[5];
    private int pasoActual = 0;

    // Nombres de los pasos
    private static final String[] PASOS = {
        "1  Registro", "2  Producto", "3  Cita", "4  Pago", "5  Resumen"
    };

    public MainView(Stage stage) {
        this.stage = stage;
        this.tienda = new Tienda("VisionMaster – Sucursal Centro");
        this.root = new BorderPane();
        root.getStyleClass().add("main-root");

        root.setTop(buildHeader());
        root.setLeft(buildStepper());
        mostrarPaso(0);
    }

    // ── Header ──────────────────────────────────────────────────
    private HBox buildHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 24, 0, 24));
        header.setPrefHeight(60);

        Label logo = new Label("VisionMaster");
        logo.getStyleClass().add("header-logo");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label sucursal = new Label(tienda.getNombreSucursal());
        sucursal.getStyleClass().add("header-sub");

        header.getChildren().addAll(logo, spacer, sucursal);
        return header;
    }

    // ── Stepper lateral ─────────────────────────────────────────
    private VBox buildStepper() {
        VBox stepper = new VBox(6);
        stepper.getStyleClass().add("stepper");
        stepper.setPrefWidth(170);
        stepper.setPadding(new Insets(32, 16, 16, 16));

        for (int i = 0; i < PASOS.length; i++) {
            Label lbl = new Label(PASOS[i]);
            lbl.getStyleClass().addAll("step-label", "step-inactive");
            lbl.setMaxWidth(Double.MAX_VALUE);
            stepLabels[i] = lbl;
            stepper.getChildren().add(lbl);
        }
        return stepper;
    }

    // ── Navegación entre pasos ───────────────────────────────────
    public void mostrarPaso(int paso) {
        pasoActual = paso;
        actualizarStepper();

        switch (paso) {
            case 0 -> root.setCenter(new RegistroView(this).getView());
            case 1 -> root.setCenter(new ProductoView(this).getView());
            case 2 -> root.setCenter(new CitaView(this).getView());
            case 3 -> root.setCenter(new PagoView(this).getView());
            case 4 -> root.setCenter(new ResumenView(this, stage).getView());
        }
    }

    private void actualizarStepper() {
        for (int i = 0; i < stepLabels.length; i++) {
            stepLabels[i].getStyleClass().removeAll("step-active", "step-done", "step-inactive");
            if (i < pasoActual)       stepLabels[i].getStyleClass().add("step-done");
            else if (i == pasoActual) stepLabels[i].getStyleClass().add("step-active");
            else                      stepLabels[i].getStyleClass().add("step-inactive");
        }
    }

    // ── Botones de navegación estándar ───────────────────────────
    public HBox buildNavButtons(String labelAnterior, String labelSiguiente,
                                Runnable accionAnterior, Runnable accionSiguiente) {
        HBox nav = new HBox(12);
        nav.setAlignment(Pos.CENTER_RIGHT);
        nav.setPadding(new Insets(16, 0, 0, 0));

        if (labelAnterior != null) {
            Button btnAnterior = new Button(labelAnterior);
            btnAnterior.getStyleClass().add("btn-secondary");
            btnAnterior.setOnAction(e -> accionAnterior.run());
            nav.getChildren().add(btnAnterior);
        }

        if (labelSiguiente != null) {
            Button btnSiguiente = new Button(labelSiguiente);
            btnSiguiente.getStyleClass().add("btn-primary");
            btnSiguiente.setOnAction(e -> accionSiguiente.run());
            nav.getChildren().add(btnSiguiente);
        }
        return nav;
    }

    // ── Getters / Setters compartidos ───────────────────────────
    public Tienda getTienda()                       { return tienda; }
    public Cliente getClienteActual()               { return clienteActual; }
    public void setClienteActual(Cliente c)         { this.clienteActual = c; }
    public OrdenCompra getOrdenActual()             { return ordenActual; }
    public void setOrdenActual(OrdenCompra o)       { this.ordenActual = o; }
    public Stage getStage()                         { return stage; }
    public BorderPane getRoot()                     { return root; }
}
