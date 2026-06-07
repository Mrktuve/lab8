package client.gui;

import client.NetworkClient;
import common.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Главный экран приложения с таблицей работников и панелью визуализации.
 */
public class MainView {

    private final Stage stage;
    private final NetworkClient networkClient;
    private final Localization localization;
    private final Session session;
    private final ClientService clientService;

    private TableView<Worker> tableView;
    private ObservableList<Worker> observableList;
    private CanvasPanel canvasPanel;
    private TextArea infoArea;
    private Label statusLabel;

    public MainView(Stage stage, NetworkClient networkClient, Localization localization, Session session) {
        this.stage = stage;
        this.networkClient = networkClient;
        this.localization = localization;
        this.session = session;
        this.clientService = new ClientService(networkClient, session, localization);
    }

    /**
     * Создает и показывает главный экран.
     */
    public void show() {
        BorderPane root = new BorderPane();

        // Верхняя панель с кнопками
        VBox topPanel = createTopPanel();

        // Таблица работников
        tableView = createTableView();

        // Нижняя панель с canvas и информацией
        HBox bottomPanel = createBottomPanel();

        root.setTop(topPanel);
        root.setCenter(tableView);
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setTitle(localization.get("main.title") + " - " + session.getLogin());
        stage.setMaximized(true);
        stage.show();

        // Загружаем коллекцию при старте
        refreshCollection();
    }

    private VBox createTopPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #d3d3d3;");

        // Панель с кнопками управления
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button(localization.get("button.add"));
        addButton.setOnAction(e -> handleAdd());

        Button updateButton = new Button(localization.get("button.update"));
        updateButton.setOnAction(e -> handleUpdate());

        Button removeButton = new Button(localization.get("button.remove"));
        removeButton.setOnAction(e -> handleRemove());

        Button clearButton = new Button(localization.get("button.clear"));
        clearButton.setOnAction(e -> handleClear());

        Button refreshButton = new Button(localization.get("button.refresh"));
        refreshButton.setOnAction(e -> refreshCollection());

        Button infoButton = new Button(localization.get("button.info"));
        infoButton.setOnAction(e -> showInfo());

        Button helpButton = new Button(localization.get("button.help"));
        helpButton.setOnAction(e -> showHelp());

        Button logoutButton = new Button(localization.get("button.logout"));
        logoutButton.setOnAction(e -> handleLogout());

        buttonBox.getChildren().addAll(addButton, updateButton, removeButton, clearButton,
                refreshButton, infoButton, helpButton, logoutButton);

        // Панель с фильтром
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER);

        Label filterLabel = new Label(localization.get("main.filter") + ":");
        TextField filterField = new TextField();
        filterField.setPromptText(localization.get("main.filter.name"));
        filterField.setPrefWidth(200);
        filterField.textProperty().addListener((obs, oldVal, newVal) -> filterByName(newVal));

        filterBox.getChildren().addAll(filterLabel, filterField);

        panel.getChildren().addAll(buttonBox, filterBox);
        return panel;
    }

    private TableView<Worker> createTableView() {
        tableView = new TableView<>();
        observableList = FXCollections.observableArrayList();
        tableView.setItems(observableList);

        // Колонки
        TableColumn<Worker, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Worker, String> nameCol = new TableColumn<>(localization.get("worker.name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Worker, Double> salaryCol = new TableColumn<>(localization.get("worker.salary"));
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryCol.setPrefWidth(100);

        TableColumn<Worker, String> dateCol = new TableColumn<>(localization.get("worker.creationDate"));
        dateCol.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getCreationDate();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? Formats.formatDate(date, localization.getCurrentLocale()) : ""
            );
        });
        dateCol.setPrefWidth(100);

        TableColumn<Worker, String> statusCol = new TableColumn<>(localization.get("worker.status"));
        statusCol.setCellValueFactory(cellData -> {
            var status = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(
                    status != null ? localization.get("status." + status.name().toLowerCase()) : ""
            );
        });
        statusCol.setPrefWidth(100);

        TableColumn<Worker, String> ownerCol = new TableColumn<>(localization.get("worker.owner"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerLogin"));
        ownerCol.setPrefWidth(100);

        tableView.getColumns().addAll(idCol, nameCol, salaryCol, dateCol, statusCol, ownerCol);

        // Двойной клик для редактирования
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Worker selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    handleEdit(selected);
                }
            }
        });

        return tableView;
    }

    private HBox createBottomPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));

        // Canvas для визуализации
        canvasPanel = new CanvasPanel(localization);
        canvasPanel.setPrefSize(400, 300);
        canvasPanel.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        // Информационная панель
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(400);

        Label infoLabel = new Label(localization.get("main.info") + ":");
        infoLabel.setStyle("-fx-font-weight: bold;");

        infoArea = new TextArea();
        infoArea.setPrefHeight(200);
        infoArea.setEditable(false);
        infoArea.setWrapText(true);

        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

        infoBox.getChildren().addAll(infoLabel, infoArea, statusLabel);

        panel.getChildren().addAll(canvasPanel, infoBox);
        return panel;
    }

    // Обработчики кнопок
    private void handleAdd() {
        WorkerDialog dialog = new WorkerDialog(localization, null);
        dialog.showAndWait().ifPresent(worker -> {
            var response = clientService.addWorker(worker);
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.add"));
            } else {
                showError(response.getMessage());
            }
        });
    }

    private void handleEdit(Worker worker) {
        WorkerDialog dialog = new WorkerDialog(localization, worker);
        dialog.showAndWait().ifPresent(updatedWorker -> {
            var response = clientService.updateWorker(worker.getId(), updatedWorker);
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.update"));
            } else {
                showError(response.getMessage());
            }
        });
    }

    private void handleUpdate() {
        Worker selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleEdit(selected);
        } else {
            showError(localization.get("error.select.worker"));
        }
    }

    private void handleRemove() {
        Worker selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            boolean confirmed = InputDialogs.showConfirmationDialog(
                    localization.get("dialog.confirm"),
                    localization.get("dialog.remove.confirm"),
                    localization.get("worker.id") + ": " + selected.getId()
            );
            if (confirmed) {
                var response = clientService.removeById(selected.getId());
                if (response.isSuccess()) {
                    refreshCollection();
                    showSuccess(localization.get("success.remove"));
                } else {
                    showError(response.getMessage());
                }
            }
        } else {
            showError(localization.get("error.select.worker"));
        }
    }

    private void handleClear() {
        boolean confirmed = InputDialogs.showConfirmationDialog(
                localization.get("dialog.confirm"),
                localization.get("dialog.clear.confirm"),
                localization.get("dialog.clear.warning")
        );
        if (confirmed) {
            var response = clientService.clearCollection();
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.clear"));
            } else {
                showError(response.getMessage());
            }
        }
    }

    private void refreshCollection() {
        List<Worker> workers = clientService.loadCollection();
        if (workers != null) {
            observableList.setAll(workers);
            canvasPanel.setWorkers(workers);
            updateInfo(workers);
            showSuccess(localization.get("success.refresh"));
        } else {
            showError(localization.get("error.load.collection"));
        }
    }

    private void filterByName(String name) {
        if (name == null || name.isEmpty()) {
            refreshCollection();
            return;
        }

        List<Worker> filtered = CollectionUtils.filterStartsWithName(
                observableList.getAll(), name
        );
        observableList.setAll(filtered);
        canvasPanel.setWorkers(filtered);
    }

    private void showInfo() {
        List<Worker> workers = observableList.getAll();
        updateInfo(workers);
    }

    private void showHelp() {
        String helpText = localization.get("help.text");
        InputDialogs.showInfoDialog(
                localization.get("button.help"),
                localization.get("help.title"),
                helpText
        );
    }

    private void handleLogout() {
        boolean confirmed = InputDialogs.showConfirmationDialog(
                localization.get("dialog.confirm"),
                localization.get("dialog.logout"),
                localization.get("dialog.logout.confirm")
        );
        if (confirmed) {
            session.clear();
            networkClient.close();

            // Возвращаемся к окну авторизации
            try {
                NetworkClient newClient = new NetworkClient("localhost", 2222);
                Session newSession = new Session();
                Localization newLocalization = new Localization();

                AuthView authView = new AuthView(stage, newClient, newLocalization, newSession);
                authView.show();
            } catch (Exception e) {
                showError("Failed to reconnect: " + e.getMessage());
            }
        }
    }

    private void updateInfo(List<Worker> workers) {
        StringBuilder sb = new StringBuilder();
        sb.append(localization.get("info.total")).append(": ").append(workers.size()).append("\n");

        if (!workers.isEmpty()) {
            long uniqueOwners = workers.stream()
                    .map(Worker::getOwnerLogin)
                    .filter(java.util.Objects::nonNull)
                    .distinct().count();
            sb.append(localization.get("info.owners")).append(": ").append(uniqueOwners).append("\n");

            double avgSalary = workers.stream()
                    .mapToDouble(w -> w.getSalary() != null ? w.getSalary() : 0)
                    .average()
                    .orElse(0);
            sb.append(localization.get("info.avg_salary")).append(": ")
                    .append(String.format("%.2f", avgSalary)).append("\n");
        }

        infoArea.setText(sb.toString());
        statusLabel.setText(localization.get("status.ready"));
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
    }

    private void showError(String message) {
        InputDialogs.showErrorDialog(
                localization.get("dialog.error"),
                localization.get("dialog.error"),
                message
        );
        statusLabel.setText(localization.get("status.error"));
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
    }
}