package client.gui;

import client.NetworkClient;
import common.network.Request;
import common.network.Response;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Экран авторизации и регистрации пользователя.
 */
public class AuthView {

    private final Stage stage;
    private final NetworkClient networkClient;
    private final Localization localization;
    private final Session session;

    private TextField loginField;
    private PasswordField passwordField;
    private Label errorLabel;
    private Button loginButton;
    private Button registerButton;
    private ComboBox<String> languageSelector;

    public AuthView(Stage stage, NetworkClient networkClient, Localization localization, Session session) {
        this.stage = stage;
        this.networkClient = networkClient;
        this.localization = localization;
        this.session = session;
    }

    /**
     * Создаёт и показывает окно авторизации.
     */
    public void show() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Заголовок
        Label titleLabel = new Label(localization.get("auth.title"));
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Выбор языка
        HBox languageBox = new HBox(10);
        languageBox.setAlignment(Pos.CENTER);
        Label languageLabel = new Label(localization.get("auth.language") + ":");
        languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll("Русский", "Eesti", "Lietuvių", "Español (CR)");
        languageSelector.setValue("Русский");
        languageSelector.setOnAction(e -> changeLanguage());
        languageBox.getChildren().addAll(languageLabel, languageSelector);

        // Поля ввода
        loginField = new TextField();
        loginField.setPromptText(localization.get("auth.login.prompt"));
        loginField.setPrefWidth(300);

        passwordField = new PasswordField();
        passwordField.setPromptText(localization.get("auth.password.prompt"));
        passwordField.setPrefWidth(300);

        // Кнопки
        loginButton = new Button(localization.get("auth.login.button"));
        loginButton.setPrefWidth(140);
        loginButton.setOnAction(e -> handleLogin());

        registerButton = new Button(localization.get("auth.register.button"));
        registerButton.setPrefWidth(140);
        registerButton.setOnAction(e -> handleRegister());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, registerButton);

        // Ошибки
        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);

        root.getChildren().addAll(titleLabel, languageBox, loginField, passwordField, buttonBox, errorLabel);

        Scene scene = new Scene(root, 400, 350);
        stage.setScene(scene);
        stage.setTitle(localization.get("auth.window.title"));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Обрабатывает попытку входа в систему.
     */
    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showError(localization.get("auth.error.empty"));
            return;
        }

        try {
            Request request = new Request("login", login, password, null);
            Response response = networkClient.sendRequest(request);

            if (response.isSuccess()) {
                session.setLogin(login);
                showError(""); // Очищаем ошибки
                openMainView();
            } else {
                showError(response.getMessage());
            }
        } catch (Exception e) {
            showError(localization.get("auth.error.network") + ": " + e.getMessage());
        }
    }

    /**
     * Обрабатывает попытку регистрации.
     */
    private void handleRegister() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            showError(localization.get("auth.error.empty"));
            return;
        }

        try {
            Request request = new Request("register", login, password, null);
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

    /**
     * Переключает язык интерфейса.
     */
    private void changeLanguage() {
        String selected = languageSelector.getValue();
        String langCode = switch (selected) {
            case "Eesti" -> "et";
            case "Lietuvių" -> "lt";
            case "Español (CR)" -> "es_CR";
            default -> "ru";
        };

        localization.setLocale(langCode);
        show(); // Перерисовываем окно
    }

    /**
     * Открывает главное окно приложения.
     */
    private void openMainView() {
        MainView mainView = new MainView(stage, networkClient, localization, session);
        mainView.show();
    }

    /**
     * Показывает сообщение об ошибке.
     */
    private void showError(String message) {
        errorLabel.setText(message);
    }

    /**
     * Показывает диалоговое окно.
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(localization.get("dialog.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}