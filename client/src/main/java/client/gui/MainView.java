package client.gui;

import client.NetworkClient;
import client.gui.localization.localization;
import common.model.*;
import common.enums.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;


public class MainView {
    private final Stage stage;
    private final NetworkClient networkClient;
    private final localization localization;
    private final Session session;
    private final ClientService clientService;

    private TableView<Worker> tableView;
    private ObservableList<Worker> observableList;
    private ObservableList<Worker> fullList;
    private CanvasPanel canvasPanel;
    private TextArea infoArea;
    private Label statusLabel;

    public MainView(Stage stage, NetworkClient networkClient, localization localization, Session session) {
        this.stage = stage;
        this.networkClient = networkClient;
        this.localization = localization;
        this.session = session;
        this.clientService = new ClientService(networkClient, session, localization);
    }

    private void styleButton(Button button) {

        button.setFont(MinecraftFont.get(12));

        button.setPrefHeight(36);

        button.setStyle("""
                -fx-background-color:
                #7a7a7a;
                
                -fx-text-fill:
                white;
                
                -fx-border-color:
                #2f2f2f;
                
                -fx-border-width:
                2;
                """);
    }

    public void show() {

        BorderPane content = new BorderPane();

        content.setTop(createTopPanel());

        content.setCenter(createCenter());


        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/background.png")));

        ImageView backgroundView = new ImageView(backgroundImage);

        backgroundView.setPreserveRatio(false);


        StackPane root = new StackPane(backgroundView, content);

        Scene scene = new Scene(root, 1920, 1080);


        backgroundView.fitWidthProperty().bind(scene.widthProperty());

        backgroundView.fitHeightProperty().bind(scene.heightProperty());

        stage.setScene(scene);

        stage.setTitle(localization.get("main.title") + " - " + session.getLogin());

        stage.setMaximized(true);

        stage.show();

        refreshCollection();
    }

    private VBox createTopPanel() {

        VBox panel = new VBox(8);

        panel.setPadding(new Insets(10));

        panel.setStyle("""
                -fx-background-color:
                rgba(25,25,25,0.75);
                
                -fx-border-color:
                #4d4d4d;
                
                -fx-border-width:
                0 0 2 0;
                """);



        HBox buttonBox = new HBox(8);

        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = createButton("add", e -> handleAdd());

        Button addIfMaxButton = createButton("add_if_max", e -> handleAddIfMax());

        Button updateButton = createButton("update", e -> handleUpdate());

        Button removeButton = createButton("remove", e -> handleRemove());

        Button clearButton = createButton("clear", e -> handleClear());

        Button refreshButton = createButton("refresh", e -> refreshCollection());

        Button infoButton = createButton("info", e -> showInfo());

        Button removeLowerButton = createButton("remove_lower", e -> handleRemoveLower());

        Button removeByStatusButton = createButton("remove_by_status", e -> handleRemoveByStatus());

        Button helpButton = createButton("help", e -> handleHelp());

        Button logoutButton = createButton("logout", e -> handleLogout());

        // Minecraft style
        styleButton(addButton);
        styleButton(addIfMaxButton);
        styleButton(updateButton);
        styleButton(removeButton);
        styleButton(clearButton);
        styleButton(refreshButton);
        styleButton(infoButton);
        styleButton(removeLowerButton);
        styleButton(removeByStatusButton);
        styleButton(helpButton);
        styleButton(logoutButton);

        buttonBox.getChildren().addAll(addButton, addIfMaxButton, updateButton, removeButton, clearButton, refreshButton, infoButton, removeLowerButton, removeByStatusButton, helpButton, logoutButton);


        HBox filterBox = new HBox(10);

        filterBox.setAlignment(Pos.CENTER);

        Label filterLabel = new Label(localization.get("main.filter") + ":");

        filterLabel.setStyle("-fx-text-fill: white;");

        filterLabel.setFont(MinecraftFont.get(14));

        TextField filterField = new TextField();

        filterField.setPromptText(localization.get("main.filter.name"));

        filterField.setPrefWidth(220);

        filterField.textProperty().addListener((obs, oldVal, newVal) -> filterByName(newVal));

        Label sortLabel = new Label(localization.get("main.sort") + ":");

        sortLabel.setStyle("-fx-text-fill: white;");

        sortLabel.setFont(MinecraftFont.get(14));

        ComboBox<String> sortCombo = new ComboBox<>();

        sortCombo.getItems().addAll(localization.get("sort.name_asc"), localization.get("sort.name_desc"), localization.get("sort.salary_asc"), localization.get("sort.salary_desc"), localization.get("sort.id_asc"));

        sortCombo.setValue(localization.get("sort.id_asc"));

        sortCombo.setOnAction(e -> sortBy(sortCombo.getValue()));

        filterBox.getChildren().addAll(filterLabel, filterField, sortLabel, sortCombo);

        panel.getChildren().addAll(buttonBox, filterBox);

        return panel;
    }

    private void applyNormalButtonStyle(Button button) {

        button.setStyle("""
                -fx-background-color:
                #7a7a7a;
                
                -fx-text-fill:
                white;
                
                -fx-border-color:
                #2c2c2c;
                
                -fx-border-width:
                2;
                
                -fx-background-radius:
                0;
                
                -fx-border-radius:
                0;
                """);
    }

    private void applyHoverButtonStyle(Button button) {

        button.setStyle("""
                -fx-background-color:
                #9b9b9b;
                
                -fx-text-fill:
                white;
                
                -fx-border-color:
                #2c2c2c;
                
                -fx-border-width:
                2;
                
                -fx-background-radius:
                0;
                
                -fx-border-radius:
                0;
                """);
    }

    private Button createButton(String key, EventHandler<ActionEvent> action) {

        Button button = new Button(localization.get(key));

        button.setOnAction(action);

        button.setFont(MinecraftFont.get(11));

        button.setPrefSize(145, 38);

        applyNormalButtonStyle(button);

        button.setOnMouseEntered(e -> applyHoverButtonStyle(button));

        button.setOnMouseExited(e -> applyNormalButtonStyle(button));

        button.setOnMousePressed(e -> button.setTranslateY(2));

        button.setOnMouseReleased(e -> button.setTranslateY(0));

        return button;
    }

    private Region createCenter() {

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.6);

        tableView = new TableView<>();
        observableList = FXCollections.observableArrayList();
        fullList = FXCollections.observableArrayList();

        tableView.setItems(observableList);

        TableColumn<Worker, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Worker, String> nameCol = new TableColumn<>(localization.get("worker.name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Worker, String> coordCol = new TableColumn<>(localization.get("worker.coordinates"));
        coordCol.setCellValueFactory(cellData -> {
            Coordinates c = cellData.getValue().getCoordinates();
            return new javafx.beans.property.SimpleStringProperty(c != null ? "(" + c.getX() + ", " + c.getY() + ")" : "");
        });
        TableColumn<Worker, String> dateCol = new TableColumn<>(localization.get("worker.creationDate"));

        dateCol.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getCreationDate();

            return new javafx.beans.property.SimpleStringProperty(date != null ? Formats.formatDate(LocalDate.from(date), localization.getCurrentLocale()) : "");
        });

        TableColumn<Worker, String> salaryCol = new TableColumn<>(localization.get("worker.salary"));
        salaryCol.setCellValueFactory(cellData -> {
            Double s = cellData.getValue().getSalary();
            return new javafx.beans.property.SimpleStringProperty(s != null ? Formats.formatNumber(s, localization.getCurrentLocale()) : "");
        });

        TableColumn<Worker, String> startCol = new TableColumn<>(localization.get("worker.startDate"));

        startCol.setCellValueFactory(cellData -> {
            var d = cellData.getValue().getStartDate();

            return new javafx.beans.property.SimpleStringProperty(d != null ? Formats.formatDate(d, localization.getCurrentLocale()) : "");
        });
        TableColumn<Worker, String> endCol = new TableColumn<>(localization.get("worker.endDate"));

        endCol.setCellValueFactory(cellData -> {
            var d = cellData.getValue().getEndDate();

            return new javafx.beans.property.SimpleStringProperty(d != null ? Formats.formatDate(LocalDate.from(d), localization.getCurrentLocale()) : "");
        });

        TableColumn<Worker, String> statusCol = new TableColumn<>(localization.get("worker.status"));
        statusCol.setCellValueFactory(cellData -> {
            var status = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(status != null ? formatStatus(status) : "");
        });
        TableColumn<Worker, String> passportCol = new TableColumn<>(localization.get("person.passportID"));

        passportCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();

            return new javafx.beans.property.SimpleStringProperty(p != null && p.getPassportID() != null ? p.getPassportID() : "");
        });
        TableColumn<Worker, String> eyeCol = new TableColumn<>(localization.get("person.eyeColor"));

        eyeCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();

            return new javafx.beans.property.SimpleStringProperty(p != null && p.getEyeColor() != null ? p.getEyeColor().name() : "");
        });
        TableColumn<Worker, String> hairCol = new TableColumn<>(localization.get("person.hairColor"));

        hairCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();

            return new javafx.beans.property.SimpleStringProperty(p != null && p.getHairColor() != null ? p.getHairColor().name() : "");
        });
        TableColumn<Worker, String> natCol = new TableColumn<>(localization.get("person.nationality"));

        natCol.setCellValueFactory(cellData -> {
            Person p = cellData.getValue().getPerson();

            return new javafx.beans.property.SimpleStringProperty(p != null && p.getNationality() != null ? p.getNationality().name() : "");
        });

        TableColumn<Worker, String> ownerCol = new TableColumn<>(localization.get("worker.owner"));
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("ownerLogin"));

        tableView.getColumns().addAll(idCol, nameCol, coordCol, dateCol, salaryCol, startCol, endCol, statusCol, passportCol, eyeCol, hairCol, natCol, ownerCol);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Worker selected = tableView.getSelectionModel().getSelectedItem();

                if (selected != null) {
                    handleEdit(selected);
                }
            }
        });

        VBox tableBox = new VBox(tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);


        canvasPanel = new CanvasPanel(localization, session);
        canvasPanel.setOnWorkerClick(this::showWorkerInfo);

        VBox.setVgrow(canvasPanel, Priority.ALWAYS);

        Label infoLabel = new Label(localization.get("main.info"));

        infoArea = new TextArea();
        infoArea.setEditable(false);
        infoArea.setWrapText(true);
        infoArea.setPrefHeight(90);
        infoArea.setMaxHeight(90);

        statusLabel = new Label();
        statusLabel.setMaxHeight(25);

        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));

        rightPanel.getChildren().addAll(canvasPanel, infoLabel, infoArea, statusLabel);

        VBox.setVgrow(canvasPanel, Priority.ALWAYS);

        splitPane.getItems().addAll(tableBox, rightPanel);

        return splitPane;
    }


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


    private void filterByName(String name) {
        if (name == null || name.isEmpty()) {
            observableList.setAll(fullList);
            canvasPanel.setWorkers(fullList);
            return;
        }
        String lower = name.toLowerCase();
        List<Worker> filtered = fullList.stream().filter(w -> w.getName() != null && w.getName().toLowerCase().startsWith(lower)).collect(Collectors.toList());
        observableList.setAll(filtered);
        canvasPanel.setWorkers(filtered);
    }


    private void refreshCollection() {
        showSuccess(localization.get("status.loading"));
        Task<List<Worker>> task = new Task<>() {
            @Override
            protected List<Worker> call() {
                for (int i = 0; i < 3; i++) {
                    List<Worker> workers = clientService.loadCollection();
                    if (workers != null) return workers;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
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
            System.out.println(response.getMessage());
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.add"));
            } else showError(response.getMessage());
        });
    }

    private void handleAddIfMax() {
        WorkerDialog dialog = new WorkerDialog(localization, session, null);
        dialog.showAndWait().ifPresent(worker -> {
            var response = clientService.addIfMax(worker);
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.add_if_max"));
            } else showError(response.getMessage());
        });
    }

    private void handleEdit(Worker worker) {
        WorkerDialog dialog = new WorkerDialog(localization, session, worker);
        dialog.showAndWait().ifPresent(updated -> {
            if (worker.getId() != null) {
                var response = clientService.updateWorker(worker.getId(), updated);
                if (response.isSuccess()) {
                    refreshCollection();
                    showSuccess(localization.get("success.update"));
                } else showError(response.getMessage());
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
            if (InputDialogs.showConfirmationDialog(localization.get("dialog.confirm"), localization.get("dialog.remove.confirm"), localization.get("worker.id") + ": " + selected.getId())) {
                var response = clientService.removeById(selected.getId());
                if (response.isSuccess()) {
                    refreshCollection();
                    showSuccess(localization.get("success.remove"));
                } else showError(response.getMessage());
            }
        } else showError(localization.get("error.select.worker"));
    }

    private void handleClear() {
        if (InputDialogs.showConfirmationDialog(localization.get("dialog.confirm"), localization.get("dialog.clear.confirm"), localization.get("dialog.clear.warning"))) {
            var response = clientService.clearCollection();
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.clear"));
            } else showError(response.getMessage());
        }
    }

    private void handleRemoveLower() {
        WorkerDialog dialog = new WorkerDialog(localization, session, null);
        dialog.showAndWait().ifPresent(worker -> {
            var response = clientService.removeLower(worker);
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.remove_lower"));
            } else showError(response.getMessage());
        });
    }

    private void handleRemoveByStatus() {
        Status status = InputDialogs.showChoiceDialog(localization.get("dialog.confirm"), localization.get("button.remove_by_status"), localization.get("worker.status"), java.util.Arrays.asList(Status.values()));
        if (status != null) {
            var response = clientService.removeAnyByStatus(status);
            if (response.isSuccess()) {
                refreshCollection();
                showSuccess(localization.get("success.remove_by_status"));
            } else showError(response.getMessage());
        }
    }


    private void handleHelp() {
        StringBuilder helpText = new StringBuilder();
        helpText.append(localization.get("help.add")).append("\n\n");
        helpText.append(localization.get("help.add_if_max")).append("\n\n");
        helpText.append(localization.get("help.update")).append("\n\n");
        helpText.append(localization.get("help.remove")).append("\n\n");
        helpText.append(localization.get("help.clear")).append("\n\n");
        helpText.append(localization.get("help.refresh")).append("\n\n");
        helpText.append(localization.get("help.info")).append("\n\n");
        helpText.append(localization.get("help.removelower")).append("\n\n");
        helpText.append(localization.get("help.remove_by_status")).append("\n\n");
        helpText.append(localization.get("help.logout")).append("\n\n");

        InputDialogs.showInfoDialog(
                localization.get("dialog.help"),
                localization.get("button.help"),

                helpText.toString()
        );

    }

    private void showInfo() {
        var response = clientService.info();
        if (response.isSuccess()) infoArea.setText(response.getMessage());
    }


    private void showWorkerInfo(Worker worker) {
        infoArea.setText(CollectionUtils.formatWorker(worker, localization));
    }

    private void handleLogout() {
        if (InputDialogs.showConfirmationDialog(localization.get("dialog.confirm"), localization.get("dialog.logout"), localization.get("dialog.logout.confirm"))) {
            session.clear();
            networkClient.close();
            try {
                NetworkClient newClient = new NetworkClient("localhost", 2222);
                Session newSession = new Session();
                localization newLoc = new localization();
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
            long uniqueOwners = workers.stream().map(Worker::getOwnerLogin).filter(java.util.Objects::nonNull).distinct().count();
            sb.append(localization.get("info.owners")).append(": ").append(uniqueOwners).append("\n");
            double avg = workers.stream().mapToDouble(w -> w.getSalary() != null ? w.getSalary() : 0).average().orElse(0);
            sb.append(localization.get("info.avg_salary")).append(": ").append(String.format("%.2f", avg)).append("\n");
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