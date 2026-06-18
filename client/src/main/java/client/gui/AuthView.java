package client.gui;

import client.NetworkClient;
import client.gui.localization.localization;
import common.network.Request;
import common.network.Response;
import common.commands.Login;
import common.commands.Register;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Экран авторизации и регистрации.
 */
public class AuthView {

    private final Stage stage;
    private final NetworkClient networkClient;
    private final localization localization;
    private final Session session;

    private TextField loginField;
    private PasswordField passwordField;
    private Label errorLabel;
    private ComboBox<String> languageSelector;

    public AuthView(Stage stage, NetworkClient networkClient, localization localization, Session session) {
        this.stage = stage;
        this.networkClient = networkClient;
        this.localization = localization;
        this.session = session;
    }

    public void show() {

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/title_screen.jpg")));

        ImageView backgroundView = new ImageView(backgroundImage);

        backgroundView.setPreserveRatio(false);


        Label titleLabel = new Label(localization.get("auth.title"));

        titleLabel.setFont(MinecraftFont.get(28));

        titleLabel.setStyle("-fx-text-fill: white;");



        languageSelector = new ComboBox<>();

        languageSelector.getItems().addAll(localization.get("lang.ru"), localization.get("lang.et"), localization.get("lang.lt"), localization.get("lang.es"));

        String currentLang = localization.getCurrentLocale();

        String selectedValue = switch (currentLang) {
            case "et" -> localization.get("lang.et");
            case "lt" -> localization.get("lang.lt");
            case "es_CR" -> localization.get("lang.es");
            default -> localization.get("lang.ru");
        };

        languageSelector.setValue(selectedValue);

        languageSelector.setOnAction(e -> changeLanguage());

        languageSelector.setPrefWidth(220);



        loginField = new TextField();

        loginField.setPromptText(localization.get("auth.login.prompt"));

        loginField.setFont(MinecraftFont.get(18));
        loginField.setStyle("""
                -fx-background-color:
                #8b8b8b;
                
                -fx-text-fill:
                white;
                
                -fx-prompt-text-fill:
                #d0d0d0;
                
                -fx-border-color:
                #2c2c2c;
                
                -fx-border-width:
                2;
                """);


        loginField.setPrefWidth(320);
        loginField.setMaxWidth(320);
        loginField.setPrefHeight(45);
        loginField.setMaxHeight(45);



        passwordField = new PasswordField();

        passwordField.setPromptText(localization.get("auth.password.prompt"));

        passwordField.setFont(MinecraftFont.get(18));
        passwordField.setStyle("""
                -fx-background-color:
                #8b8b8b;
                
                -fx-text-fill:
                white;
                
                -fx-prompt-text-fill:
                #d0d0d0;
                
                -fx-border-color:
                #2c2c2c;
                
                -fx-border-width:
                2;
                """);

        passwordField.setPrefWidth(320);
        passwordField.setMaxWidth(320);
        passwordField.setPrefHeight(45);
        passwordField.setMaxHeight(45);


        Button loginButton = new Button(localization.get("auth.login.button"));

        loginButton.setFont(MinecraftFont.get(18));
        loginButton.setStyle("""
                -fx-background-color:
                #8b8b8b;
                
                -fx-text-fill:
                white;
                
                -fx-prompt-text-fill:
                #d0d0d0;
                
                -fx-border-color:
                #2c2c2c;
                
                -fx-border-width:
                2;
                """);

        loginButton.setPrefSize(320, 45);

        loginButton.setOnAction(e -> handleLogin());



        Button registerButton = new Button(localization.get("auth.register.button"));

        registerButton.setFont(MinecraftFont.get(18));
        registerButton.setStyle("""
                -fx-background-color:
                #8b8b8b;
                
                -fx-text-fill:
                white;
                
                -fx-prompt-text-fill:
                #d0d0d0;
                
                -fx-border-color:
                #2c2c2c;
                
                -fx-border-width:
                2;
                """);

        registerButton.setPrefSize(320, 45);

        registerButton.setOnAction(e -> handleRegister());



        errorLabel = new Label();

        errorLabel.setFont(MinecraftFont.get(14));

        errorLabel.setStyle("-fx-text-fill: #ff5555;");

        errorLabel.setWrapText(true);


        VBox menuBox = new VBox(12);

        menuBox.setAlignment(Pos.CENTER);

        menuBox.getChildren().addAll(titleLabel, languageSelector, loginField, passwordField, loginButton, registerButton, errorLabel);



        StackPane root = new StackPane(backgroundView, menuBox);

        Scene scene = new Scene(root, 1280, 720);

        backgroundView.fitWidthProperty().bind(scene.widthProperty());

        backgroundView.fitHeightProperty().bind(scene.heightProperty());

        stage.setScene(scene);

        stage.setTitle(localization.get("auth.window.title"));

        stage.setMinWidth(960);
        stage.setMinHeight(540);

        stage.centerOnScreen();

        stage.show();
    }

    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showError(localization.get("auth.error.empty"));
            return;
        }

        try {
            Request request = new Request(new Login(), login, password);
            Response response = networkClient.sendRequest(request);

            if (response.isSuccess()) {
                session.setLogin(login);
                session.setPassword(password);
                showError("");
                openMainView();
            } else {
                showError(response.getMessage());
            }
        } catch (Exception e) {
            showError(localization.get("auth.error.network") + ": " + e.getMessage());
        }
    }

    private void handleRegister() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showError(localization.get("auth.error.empty"));
            return;
        }

        try {
            Request request = new Request(new Register(), login, password);
            Response response = networkClient.sendRequest(request);

            if (response.isSuccess()) {
                showError("");
                showAlert(localization.get("auth.register.success"), Alert.AlertType.INFORMATION);
            } else {
                showError(response.getMessage());
            }
        } catch (Exception e) {
            showError(localization.get("auth.error.network") + ": " + e.getMessage());
        }
    }

    private void changeLanguage() {
        String selected = languageSelector.getValue();


        String langCode;
        if (selected.equals(localization.get("lang.et"))) {
            langCode = "et";
        } else if (selected.equals(localization.get("lang.lt"))) {
            langCode = "lt";
        } else if (selected.equals(localization.get("lang.es"))) {
            langCode = "es_CR";
        } else {
            langCode = "ru";
        }

        localization.setLocale(langCode);


        show();
    }

    private void openMainView() {
        MainView mainView = new MainView(stage, networkClient, localization, session);
        mainView.show();
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(localization.get("dialog.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}