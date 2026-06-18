package client.gui;

import client.NetworkClient;
import client.gui.localization.localization;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Точка входа JavaFX приложения.
 * Инициализирует соединение с сервером и показывает окно авторизации.
 */
public class GuiBootstrap extends Application {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 2222;

    @Override
    public void start(Stage primaryStage) {
        try {

            NetworkClient networkClient = new NetworkClient(DEFAULT_HOST, DEFAULT_PORT);
            Session session = new Session();
            localization localization = new localization();


            AuthView authView = new AuthView(primaryStage, networkClient, localization, session);
            authView.show();


            primaryStage.setOnCloseRequest(e -> {
                networkClient.close();
                System.exit(0);
            });

        } catch (Exception e) {
            e.printStackTrace();

            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);

            alert.setTitle("Connection Error");
            alert.setHeaderText("Failed to connect to server");
            alert.setContentText("Host: " + DEFAULT_HOST + ", Port: " + DEFAULT_PORT + "\nError: " + e.getMessage());
            alert.showAndWait();
            System.exit(1);
        }
    }
}