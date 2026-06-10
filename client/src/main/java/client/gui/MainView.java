package client.gui;

import client.NetworkClient;
import common.model.*;
import common.enums.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainView {
    private final Stage stage;
    private final NetworkClient networkClient;
    private final Localization localization;
    private final Session session;
    private final ClientService clientService;

    private TableView<Worker> tableView;
    private ObservableList<Worker> observableList;
    private ObservableList<Worker> fullList;
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
        root.setTop(createTopPanel());
        root.setCenter(createCenter());
        root.setBottom(createBottomPanel());

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

        buttonBox.getChildren().addAll(
                createButton("button.add", e -> handleAdd()),
                createButton("button.add_if_max", e -> handleAddIfMax()),
                createButton("button.update", e -> handleUpdate()),
                createButton("button.remove", e -> handleRemove()),
                createButton("button.clear", e -> handleClear()),
                createButton("button.refresh", e -> refreshCollection()),
                createButton("button.info", e -> showInfo()),
                createButton("button.help", e -> showHelp()),
                createButton("button.remove_lower", e -> handleRemoveLower()),
                createButton("button.remove_by_status", e -> handleRemoveByStatus()),
                createButton("button.print_descending", e -> handlePrintDescending()),
                createButton("button.history", e -> showHistory()),
                createButton("button.logout", e -> handleLogout())
        );

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER);

        Label filterLabel = new Label(localization.get("main.filter") + ":");
        TextField filterField = new TextField();
        filterField.setPromptText(localization.get("main.filter.name"));
        filterField.setPrefWidth(200);
        filterField.textProperty().addListener((obs, oldVal, newVal) -> filterByName(newVal));

        Label sortLabel = new Label(localization.get("main.sort") + ":");
        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll(
                localization.get("sort.name_asc"),
                localization.get("sort.name_desc"),
                localization.get("sort.salary_asc"),
                localization.get("sort.salary_desc"),
                localization.get("sort.id_asc")
        );
        sortCombo.setValue(localization.get("sort.id_asc"));
        sortCombo.setOnAction(e -> sortBy(sortCombo.getValue()));

        filterBox.getChildren().addAll(filterLabel, filterField, sortLabel, sortCombo);
        panel.getChildren().addAll(buttonBox, filterBox);
        return panel;
    }

    private Button createButton(String locKey, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button btn = new Button(localization.get(locKey));
        btn.setOnAction(handler);
        return btn;
    }

    private Region createCenter() {
        tableView = new TableView<>();
        observableList = FXCollections.observableArrayList();
        fullList = FXCollections.observableArrayList();
        tableView.setItems(observableList);

        TableColumn<Worker, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Worker, String> nameCol = new TableColumn<>(localization.get("worker.name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(120);

        TableColumn<Worker, String> coordCol = new TableColumn<>(localization.get("worker.coordinates"));
        coordCol.setCellValueFactory(cellData -> {
            Coordinates c = cellData.getValue().getCoordinates();
            return new javafx.beans.property.SimpleStringProperty(
                    c != null ? "(" + c.getX() + ", " + c.getY() + ")" : ""
            );
        });
        coordCol.setPrefWidth(90);

        TableColumn<Worker, String> dateCol = new TableColumn<>(localization.get("worker.creationDate"));
        dateCol.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getCreationDate();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? Formats.formatDate(LocalDate.from(date), localization.getCurrentLocale()) : ""
            );
        });
        dateCol.setPrefWidth(100);

        TableColumn<Worker, String> salaryCol = new TableColumn<>(localization.get("worker.salary"));
        salaryCol.setCellValueFactory(cellData -> {
            Double s = cellData.getValue().getSalary();
            return new javafx.beans.property.SimpleStringProperty(
                    s != null ? Formats.formatNumber(s, localization.getCurrentLocale()) : ""
            );
        });
        salaryCol.setPrefWidth(90);

        TableColumn<Worker, String> startCol = new TableColumn<>(localization.get("worker.startDate"));
        startCol.setCellValueFactory(cellData -> {
            var d = cellData.getValue().getStartDate();
            return new javafx.beans.property.SimpleStringProperty(
                    d != null ? Formats.formatDate(d, localization.getCurrentLocale()) : ""
            );
        });
        startCol.setPrefWidth(90);

        TableColumn<Worker, String> endCol = new TableColumn<>(localization.get("worker.endDate"));
        endCol.setCellValueFactory(cellData -> {
            var d = cellData.getValue().getEndDate();
            return new javafx.beans.property.SimpleStringProperty(
                    d != null ? Formats.formatDate(LocalDate.from(d), localization.getCurrentLocale()) : ""
            );
        });
        endCol.setPrefWidth(90);

        TableColumn<Worker, String> statusCol = new TableColumn<>(localization.get("worker.status"));
        statusCol.setCellValueFactory(cellData -> {
            var s = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(s != null ? formatStatus(s) : "");
        });
        statusCol.setPrefWidth(130);

        TableColumn<Worker, String> passportCol = new TableColumn<>(localization.get("person.passportID"));
        passportCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();
            return new javafx.beans.property.SimpleStringProperty(
                    p != null && p.getPassportID() != null ? p.getPassportID() : ""
            );
        });
        passportCol.setPrefWidth(90);

        TableColumn<Worker, String> eyeCol = new TableColumn<>(localization.get("person.eyeColor"));
        eyeCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();
            return new javafx.beans.property.SimpleStringProperty(
                    p != null && p.getEyeColor() != null ? p.getEyeColor().name() : ""
            );
        });
        eyeCol.setPrefWidth(80);

        TableColumn<Worker, String> hairCol = new TableColumn<>(localization.get("person.hairColor"));
        hairCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();
            return new javafx.beans.property.SimpleStringProperty(
                    p != null && p.getHairColor() != null ? p.getHairColor().name() : ""
            );
        });
        hairCol.setPrefWidth(80);

        TableColumn<Worker, String> natCol = new TableColumn<>(localization.get("person.nationality"));
        natCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();
            return new javafx.beans.property.SimpleStringProperty(
                    p != null && p.getNationality() != null ? p.getNationality().name() : ""
            );
        });
        natCol.setPrefWidth(90);

        TableColumn<Worker, String> ownerCol = new TableColumn<>(localization.get("worker.owner"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerLogin"));
        ownerCol.setPrefWidth(100);

        tableView.getColumns().addAll(idCol, nameCol, coordCol, dateCol, salaryCol,
                startCol, endCol, statusCol, passportCol, eyeCol, hairCol, natCol, ownerCol);

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Worker selected = tableView.getSelectionModel().getSelectedItem();
                if (selected != null) handleEdit(selected);
            }
        });

        return tableView;
    }

    private HBox createBottomPanel() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));

        canvasPanel = new CanvasPanel(localization, session);
        canvasPanel.setPrefSize(600, 300);
        canvasPanel.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        canvasPanel.setOnWorkerClick(this::showWorkerInfo);

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

    // Сортировка через Streams API
    private void sortBy(String sortType) {
        List<Worker> source = fullList.isEmpty() ? observableList : fullList;
        Comparator<Worker> cmp = null;

        if (sortType.equals(localization.get("sort.name_asc"))) {
            cmp = Comparator.comparing(Worker::getName, String.CASE_INSENSITIVE_ORDER);
        } else if (sortType.equals(localization.get("sort.name_desc"))) {
            cmp = Comparator.comparing(Worker::getName, String.CASE_INSENSITIVE_ORDER).reversed();
        } else if (sortType.equals(localization.get("sort.salary_asc"))) {
            cmp = Comparator.comparing(w -> w.getSalary() != null ? w.getSalary() : 0.0);
        } else if (sortType.equals(localization.get("sort.salary_desc"))) {
            cmp = Comparator.comparing((Worker w) -> w.getSalary() != null ? w.getSalary() : 0.0).reversed();
        } else if (sortType.equals(localization.get("sort.id_asc"))) {
            cmp = Comparator.comparing(w -> w.getId() != null ? w.getId() : 0L);
        }

        if (cmp != null) {
            List<Worker> sorted = source.stream().sorted(cmp).collect(Collectors.toList());
            observableList.setAll(sorted);
        }
    }

    // Фильтрация через Streams API
    private void filterByName(String name) {
        if (name == null || name.isEmpty()) {
            observableList.setAll(fullList);
            canvasPanel.setWorkers(fullList);
            return;
        }
        String lower = name.toLowerCase();
        List<Worker> filtered = fullList.stream()
                .filter(w -> w.getName() != null && w.getName().toLowerCase().startsWith(lower))
                .collect(Collectors.toList());
        observableList.setAll(filtered);
        canvasPanel.setWorkers(filtered);
    }

    // ИСПРАВЛЕНО: без блокировки UI через Task
    private void refreshCollection() {
        showSuccess(localization.get("status.loading"));
        Task<List<Worker>> task = new Task<>() {
            @Override
            protected List<Worker> call() {
                for (int i = 0; i < 3; i++) {
                    List<Worker> workers = clientService.loadCollection();
                    if (workers != null) return workers;
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            List<Worker> workers = task.getValue();
            if (workers != null) {
                fullList.setAll(workers);
                observableList.setAll(workers);
                canvasPanel.setWorkers(workers);
                updateInfo(workers);
                showSuccess(localization.get("success.refresh"));
            } else {
                showError(localization.get("error.load.collection"));
            }
        });
        task.setOnFailed(e -> showError(localization.get("error.load.collection")));
        new Thread(task).start();
    }

    private void handleAdd() {
        WorkerDialog dialog = new WorkerDialog(localization, session, null);
        dialog.showAndWait().ifPresent(worker -> {
            var response = clientService.addWorker(worker);
            if (response.isSuccess()) { refreshCollection(); showSuccess(localization.get("success.add")); }
            else showError(response.getMessage());
        });
    }

    private void handleAddIfMax() {
        WorkerDialog dialog = new WorkerDialog(localization, session, null);
        dialog.showAndWait().ifPresent(worker -> {
            var response = clientService.addIfMax(worker);
            if (response.isSuccess()) { refreshCollection(); showSuccess(localization.get("success.add_if_max")); }
            else showError(response.getMessage());
        });
    }

    private void handleEdit(Worker worker) {
        WorkerDialog dialog = new WorkerDialog(localization, session, worker);
        dialog.showAndWait().ifPresent(updated -> {
            if (worker.getId() != null) {
                var response = clientService.updateWorker(worker.getId(), updated);
                if (response.isSuccess()) { refreshCollection(); showSuccess(localization.get("success.update")); }
                else showError(response.getMessage());
            }
        });
    }

    private void handleUpdate() {
        Worker selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) handleEdit(selected);
        else showError(localization.get("error.select.worker"));
    }

    private void handleRemove() {
        Worker selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (InputDialogs.showConfirmationDialog(
                    localization.get("dialog.confirm"),
                    localization.get("dialog.remove.confirm"),
                    localization.get("worker.id") + ": " + selected.getId())) {
                var response = clientService.removeById(selected.getId());
                if (response.isSuccess()) { refreshCollection(); showSuccess(localization.get("success.remove")); }
                else showError(response.getMessage());
            }
        } else showError(localization.get("error.select.worker"));
    }

    private void handleClear() {
        if (InputDialogs.showConfirmationDialog(
                localization.get("dialog.confirm"),
                localization.get("dialog.clear.confirm"),
                localization.get("dialog.clear.warning"))) {
            var response = clientService.clearCollection();
            if (response.isSuccess()) { refreshCollection(); showSuccess(localization.get("success.clear")); }
            else showError(response.getMessage());
        }
    }

    private void handleRemoveLower() {
        WorkerDialog dialog = new WorkerDialog(localization, session, null);
        dialog.showAndWait().ifPresent(worker -> {
            var response = clientService.removeLower(worker);
            if (response.isSuccess()) { refreshCollection(); showSuccess(localization.get("success.remove_lower")); }
            else showError(response.getMessage());
        });
    }

    private void handleRemoveByStatus() {
        Status status = InputDialogs.showChoiceDialog(
                localization.get("dialog.confirm"),
                localization.get("button.remove_by_status"),
                localization.get("worker.status"),
                java.util.Arrays.asList(Status.values())
        );
        if (status != null) {
            var response = clientService.removeAnyByStatus(status);
            if (response.isSuccess()) { refreshCollection(); showSuccess(localization.get("success.remove_by_status")); }
            else showError(response.getMessage());
        }
    }

    private void handlePrintDescending() {
        var response = clientService.printDescending();
        if (response.isSuccess() && response.getData() != null) {
            @SuppressWarnings("unchecked")
            List<Worker> workers = (List<Worker>) response.getData();
            infoArea.setText(workers.stream()
                    .map(w -> CollectionUtils.formatWorker(w, localization))
                    .reduce("", (a, b) -> a + b));
        }
    }

    private void showHistory() {
        var response = clientService.history();
        if (response.isSuccess()) infoArea.setText(response.getMessage());
    }

    private void showInfo() {
        var response = clientService.info();
        if (response.isSuccess()) infoArea.setText(response.getMessage());
    }

    private void showHelp() {
        InputDialogs.showInfoDialog(
                localization.get("button.help"),
                localization.get("help.title"),
                localization.get("help.text")
        );
    }

    private void showWorkerInfo(Worker worker) {
        infoArea.setText(CollectionUtils.formatWorker(worker, localization));
    }

    private void handleLogout() {
        if (InputDialogs.showConfirmationDialog(
                localization.get("dialog.confirm"),
                localization.get("dialog.logout"),
                localization.get("dialog.logout.confirm"))) {
            session.clear();
            networkClient.close();
            try {
                NetworkClient newClient = new NetworkClient("localhost", 2222);
                Session newSession = new Session();
                Localization newLoc = new Localization();
                new AuthView(stage, newClient, newLoc, newSession).show();
            } catch (Exception e) {
                showError("Failed to reconnect: " + e.getMessage());
            }
        }
    }

    private String formatStatus(Status status) {
        return switch (status) {
            case FIRED -> localization.get("status.fired");
            case RECOMMENDED_FOR_PROMOTION -> localization.get("status.recommended_for_promotion");
            case REGULAR -> localization.get("status.regular");
            case PROBATION -> localization.get("status.probation");
        };
    }

    private void updateInfo(List<Worker> workers) {
        StringBuilder sb = new StringBuilder();
        sb.append(localization.get("info.total")).append(": ").append(workers.size()).append("\n");
        if (!workers.isEmpty()) {
            long uniqueOwners = workers.stream()
                    .map(Worker::getOwnerLogin).filter(java.util.Objects::nonNull).distinct().count();
            sb.append(localization.get("info.owners")).append(": ").append(uniqueOwners).append("\n");
            double avg = workers.stream()
                    .mapToDouble(w -> w.getSalary() != null ? w.getSalary() : 0).average().orElse(0);
            sb.append(localization.get("info.avg_salary")).append(": ")
                    .append(String.format("%.2f", avg)).append("\n");
        }
        infoArea.setText(sb.toString());
        statusLabel.setText(localization.get("status.ready"));
    }

    private void showSuccess(String msg) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
    }

    private void showError(String msg) {
        InputDialogs.showErrorDialog(localization.get("dialog.error"), localization.get("dialog.error"), msg);
        statusLabel.setText(localization.get("status.error"));
        statusLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
    }
}