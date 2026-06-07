package client.gui;

import client.NetworkClient;
import common.model.*;
import common.enums.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;



/**
 * Главный экран приложения.
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

    public void show() {
        BorderPane root = new BorderPane();

        VBox topPanel = createTopPanel();
        tableView = createTableView();
        HBox bottomPanel = createBottomPanel();

        root.setTop(topPanel);
        root.setCenter(tableView);
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setTitle(localization.get("main.title") + " - " + session.getLogin());
        stage.setMaximized(true);
        stage.show();

        refreshCollection();
    }

    private VBox createTopPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #d3d3d3;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button(localization.get("button.add"));
        addButton.setOnAction(e -> handleAdd());

        Button addIfMaxButton = new Button(localization.get("button.add_if_max"));
        addIfMaxButton.setOnAction(e -> handleAddIfMax());

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

        buttonBox.getChildren().addAll(addButton, addIfMaxButton, updateButton, removeButton,
                clearButton, refreshButton, infoButton, helpButton, logoutButton);

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
                    date != null ? Formats.formatDate(LocalDate.from(date), localization.getCurrentLocale()) : ""
            );
        });
        dateCol.setPrefWidth(100);

        TableColumn<Worker, String> statusCol = new TableColumn<>(localization.get("worker.status"));
        statusCol.setCellValueFactory(cellData -> {
            var status = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(
                    status != null ? formatStatus(status) : ""
            );
        });
        statusCol.setPrefWidth(150);

        TableColumn<Worker, String> ownerCol = new TableColumn<>(localization.get("worker.owner"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerLogin"));
        ownerCol.setPrefWidth(100);

        tableView.getColumns().addAll(idCol, nameCol, salaryCol, dateCol, statusCol, ownerCol);

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

        canvasPanel = new CanvasPanel(localization);
        canvasPanel.setPrefSize(400, 300);
        canvasPanel.setStyle("-fx-border-color: black; -fx-border-width: 1;");

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

    private String formatStatus(Status status) {
        return switch (status) {
            case FIRED -> localization.get("status.fired");
            case RECOMMENDED_FOR_PROMOTION -> localization.get("status.recommended_for_promotion");
            case REGULAR -> localization.get("status.regular");
            case PROBATION -> localization.get("status.probation");
        };
    }

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

    private void handleAddIfMax() {
        WorkerDialog dialog = new WorkerDialog(localization, null);
        dialog.showAndWait().ifPresent(worker -> {
            var response = clientService.addIfMax(worker);
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.add_if_max"));
            } else {
                showError(response.getMessage());
            }
        });
    }

    private void handleEdit(Worker worker) {
        WorkerDialog dialog = new WorkerDialog(localization, worker);
        dialog.showAndWait().ifPresent(updatedWorker -> {
            Long workerId = worker.getId();
            if (workerId != null) {
                var response = clientService.updateWorker(workerId, updatedWorker);
                if (response.isSuccess()) {
                    refreshCollection();
                    showSuccess(localization.get("success.update"));
                } else {
                    showError(response.getMessage());
                }
            } else {
                showError("Worker ID is null");
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
        System.out.println("[MainView] Refreshing collection...");

        for (int i = 0; i < 3; i++) {
            List<Worker> workers = clientService.loadCollection();

            if (workers != null) {
                System.out.println("[MainView] Successfully loaded " + workers.size() + " workers");
                observableList.setAll(workers);
                canvasPanel.setWorkers(workers);
                updateInfo(workers);
                showSuccess(localization.get("success.refresh"));
                return;
            }

            System.err.println("[MainView] Attempt " + (i+1) + " failed");
            if (i < 2) {
                try {
                    Thread.sleep(1000); // Ждем 1 секунду перед следующей попыткой
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Все попытки исчерпаны
        System.err.println("[MainView] All attempts failed!");
        showError(localization.get("error.load.collection"));
    }

    private void filterByName(String name) {
        if (name == null || name.isEmpty()) {
            refreshCollection();
            return;
        }

        var response = clientService.filterStartsWithName(name);
        if (response.isSuccess() && response.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Worker> filtered = (List<Worker>) response.getData();
            observableList.setAll(filtered);
            canvasPanel.setWorkers(filtered);
        }
    }

    private void showInfo() {
        var response = clientService.info();
        if (response.isSuccess()) {
            infoArea.setText(response.getMessage());
        }
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