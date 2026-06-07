package visionmaster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import visionmaster.ui.MainView;

/**
 * Punto de entrada de la aplicacin VisionMaster.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView(primaryStage);

        Scene scene = new Scene(mainView.getRoot(), 900, 680);
        scene.getStylesheets().add(
            getClass().getResource("/visionmaster/styles/app.css").toExternalForm()
        );

        primaryStage.setTitle("VisionMaster  Sistema de Gestin ptica");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(860);
        primaryStage.setMinHeight(620);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
