package client.gui;

import client.gui.localization.localization;
import common.model.*;
import common.enums.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkerDialog extends Dialog<Worker> {
    private final localization localization;
    private final Session session;
    private final boolean editMode;

    private TextField nameField;
    private TextField xField;
    private TextField yField;
    private TextField salaryField;
    private TextField passportIDField;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<Status> statusComboBox;
    private ComboBox<EyeColor> eyeColorComboBox;
    private ComboBox<HairColor> hairColorComboBox;
    private ComboBox<Country> nationalityComboBox;


    public WorkerDialog(localization localization, Session session, Worker workerToEdit) {
        this.localization = localization;
        this.session = session;
        this.editMode = (workerToEdit != null);


        setTitle(editMode ? localization.get("worker.dialog.edit.title") : localization.get("worker.dialog.add.title"));
        setHeaderText(editMode ? localization.get("worker.dialog.edit.header") : localization.get("worker.dialog.add.header"));

        initFields();
        createButtonTypes();
        createLayout(workerToEdit);

        setResultConverter(buttonType -> {

            if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Worker worker = createWorkerFromFields(workerToEdit);
                return worker;
            }

            return null;
        });
    }

    private void initFields() {
        nameField = new TextField();

        xField = new TextField();

        yField = new TextField();

        salaryField = new TextField();

        passportIDField = new TextField();

        startDatePicker = new DatePicker();

        endDatePicker = new DatePicker();

        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Status.values());

        eyeColorComboBox = new ComboBox<>();
        eyeColorComboBox.getItems().addAll(EyeColor.values());

        hairColorComboBox = new ComboBox<>();
        hairColorComboBox.getItems().addAll(HairColor.values());

        nationalityComboBox = new ComboBox<>();
        nationalityComboBox.getItems().addAll(Country.values());

    }

    private void createButtonTypes() {
        ButtonType okButton = new ButtonType(localization.get("button.ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(localization.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
    }

    private void createLayout(Worker worker) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;

        grid.add(new Label(localization.get("worker.name") + ":"), 0, row);
        grid.add(nameField, 1, row++);

        grid.add(new Label("X:"), 0, row);
        grid.add(xField, 1, row++);

        grid.add(new Label("Y:"), 0, row);
        grid.add(yField, 1, row++);

        grid.add(new Label(localization.get("worker.salary") + ":"), 0, row);
        grid.add(salaryField, 1, row++);

        grid.add(new Label(localization.get("worker.startDate") + ":"), 0, row);
        grid.add(startDatePicker, 1, row++);

        grid.add(new Label(localization.get("worker.endDate") + ":"), 0, row);
        grid.add(endDatePicker, 1, row++);

        grid.add(new Label(localization.get("worker.status") + ":"), 0, row);
        grid.add(statusComboBox, 1, row++);

        Label personLabel = new Label(localization.get("worker.person") + ":");
        personLabel.setStyle("-fx-font-weight: bold;");
        grid.add(personLabel, 0, row++, 2, 1);

        grid.add(new Label(localization.get("person.passportID") + ":"), 0, row);
        grid.add(passportIDField, 1, row++);

        grid.add(new Label(localization.get("person.eyeColor") + ":"), 0, row);
        grid.add(eyeColorComboBox, 1, row++);

        grid.add(new Label(localization.get("person.hairColor") + ":"), 0, row);
        grid.add(hairColorComboBox, 1, row++);

        grid.add(new Label(localization.get("person.nationality") + ":"), 0, row);
        grid.add(nationalityComboBox, 1, row);

        if (worker != null) {
            fillFields(worker);
        }

        getDialogPane().setContent(grid);
    }

    private void fillFields(Worker worker) {
        nameField.setText(worker.getName());

        if (worker.getCoordinates() != null) {
            xField.setText(String.valueOf(worker.getCoordinates().getX()));
            yField.setText(String.valueOf(worker.getCoordinates().getY()));
        }

        if (worker.getSalary() != null) {
            salaryField.setText(String.valueOf(worker.getSalary()));
        }

        if (worker.getStartDate() != null) {
            startDatePicker.setValue(worker.getStartDate());
        }

        if (worker.getEndDate() != null) {
            endDatePicker.setValue(LocalDate.from(worker.getEndDate()));
        }

        statusComboBox.setValue(worker.getStatus());

        if (worker.getPerson() != null) {

            if (worker.getPerson().getPassportID() != null) {
                passportIDField.setText(worker.getPerson().getPassportID());
            }
            eyeColorComboBox.setValue(worker.getPerson().getEyeColor());
            hairColorComboBox.setValue(worker.getPerson().getHairColor());
            nationalityComboBox.setValue(worker.getPerson().getNationality());
        }
    }

    private Worker createWorkerFromFields(Worker existingWorker) {
        try {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException(localization.get("error.empty.name"));
            }

            int x = xField.getText().trim().isEmpty() ? 0 : Integer.parseInt(xField.getText().trim());
            long y = yField.getText().trim().isEmpty() ? 0L : Long.parseLong(yField.getText().trim());
            Coordinates coordinates = new Coordinates(x, y);

            Double salary = salaryField.getText().trim().isEmpty() ? null : Double.parseDouble(salaryField.getText().trim().replace(",", "."));
            if (salary == null || salary <= 0) {
                throw new IllegalArgumentException(localization.get("error.invalid.salary"));
            }

            LocalDate startDate = startDatePicker.getValue();
            if (startDate == null) {
                throw new IllegalArgumentException(localization.get("error.invalid.start.date"));
            }
            LocalDateTime startDateTime = startDate.atStartOfDay();

            LocalDate endDate = endDatePicker.getValue();
            LocalDateTime endDateTime = (endDate != null) ? endDate.atStartOfDay() : null;

            Status status = statusComboBox.getValue();

            Person person = null;
            if (eyeColorComboBox.getValue() != null || hairColorComboBox.getValue() != null || nationalityComboBox.getValue() != null || !passportIDField.getText().trim().isEmpty()) {
                person = new Person(passportIDField.getText().trim(),
                        eyeColorComboBox.getValue(), hairColorComboBox.getValue(), nationalityComboBox.getValue());
            }

            String ownerLogin = (existingWorker != null) ? existingWorker.getOwnerLogin() : session.getLogin();

            return new Worker(name, coordinates, salary, startDateTime, endDateTime, status, person, ownerLogin);

        } catch (NumberFormatException e) {
            InputDialogs.showErrorDialog(localization.get("dialog.error"), localization.get("error.invalid.number"), e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            InputDialogs.showErrorDialog(localization.get("dialog.error"), localization.get("dialog.error"), e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}