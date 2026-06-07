package client.gui;

import common.enums.*;
import common.model.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.awt.*;
import java.time.LocalDate;

/**
 * Диалоговое окно для создания и редактирования работника.
 */
public class WorkerDialog extends Dialog<Worker> {

    private final Localization localization;
    private final boolean editMode;

    // Поля ввода
    private TextField nameField;
    private TextField xField;
    private TextField yField;
    private TextField salaryField;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<Status> statusComboBox;
    private TextField personHeightField;
    private ComboBox<Color> eyeColorComboBox;
    private ComboBox<Color> hairColorComboBox;
    private ComboBox<Country> nationalityComboBox;

    public WorkerDialog(Localization localization, Worker workerToEdit) {
        this.localization = localization;
        this.editMode = (workerToEdit != null);

        setTitle(editMode ? localization.get("worker.dialog.edit.title")
                : localization.get("worker.dialog.add.title"));
        setHeaderText(editMode ? localization.get("worker.dialog.edit.header")
                : localization.get("worker.dialog.add.header"));

        initFields();
        createButtonTypes();
        createLayout(workerToEdit);

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return createWorkerFromFields(workerToEdit);
            }
            return null;
        });
    }

    private void initFields() {
        nameField = new TextField();
        nameField.setPromptText(localization.get("worker.name"));

        xField = new TextField();
        xField.setPromptText("X");

        yField = new TextField();
        yField.setPromptText("Y");

        salaryField = new TextField();
        salaryField.setPromptText(localization.get("worker.salary"));

        startDatePicker = new DatePicker();
        startDatePicker.setPromptText(localization.get("worker.startDate"));

        endDatePicker = new DatePicker();
        endDatePicker.setPromptText(localization.get("worker.endDate"));

        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Status.values());
        statusComboBox.setPromptText(localization.get("worker.status"));

        personHeightField = new TextField();
        personHeightField.setPromptText(localization.get("person.height"));

        eyeColorComboBox = new ComboBox<>();
        eyeColorComboBox.getItems().addAll(Color.values());
        eyeColorComboBox.setPromptText(localization.get("person.eyeColor"));

        hairColorComboBox = new ComboBox<>();
        hairColorComboBox.getItems().addAll(Color.values());
        hairColorComboBox.setPromptText(localization.get("person.hairColor"));

        nationalityComboBox = new ComboBox<>();
        nationalityComboBox.getItems().addAll(Country.values());
        nationalityComboBox.setPromptText(localization.get("person.nationality"));
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

        // Поля Person
        Label personLabel = new Label(localization.get("worker.person") + ":");
        personLabel.setStyle("-fx-font-weight: bold;");
        grid.add(personLabel, 0, row++, 2, 1);

        grid.add(new Label(localization.get("person.height") + ":"), 0, row);
        grid.add(personHeightField, 1, row++);

        grid.add(new Label(localization.get("person.eyeColor") + ":"), 0, row);
        grid.add(eyeColorComboBox, 1, row++);

        grid.add(new Label(localization.get("person.hairColor") + ":"), 0, row);
        grid.add(hairColorComboBox, 1, row++);

        grid.add(new Label(localization.get("person.nationality") + ":"), 0, row);
        grid.add(nationalityComboBox, 1, row);

        // Если редактируем, заполняем поля
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

        startDatePicker.setValue(worker.getStartDate());
        endDatePicker.setValue(worker.getEndDate());
        statusComboBox.setValue(worker.getStatus());

        if (worker.getPerson() != null) {
            personHeightField.setText(String.valueOf(worker.getPerson().getHeight()));
            eyeColorComboBox.setValue(worker.getPerson().getEyeColor());
            hairColorComboBox.setValue(worker.getPerson().getHairColor());
            nationalityComboBox.setValue(worker.getPerson().getNationality());
        }
    }

    private Worker createWorkerFromFields(Worker existingWorker) {
        try {
            Worker worker = existingWorker != null ? existingWorker : new Worker();

            // Name
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException(localization.get("error.empty.name"));
            }
            worker.setName(name);

            // Coordinates
            Double x = xField.getText().trim().isEmpty() ? null : Double.parseDouble(xField.getText().trim());
            Long y = yField.getText().trim().isEmpty() ? null : Long.parseLong(yField.getText().trim());
            if (x != null || y != null) {
                worker.setCoordinates(new Coordinates(x, y));
            }

            // Salary
            Double salary = salaryField.getText().trim().isEmpty() ? null :
                    Double.parseDouble(salaryField.getText().trim().replace(",", "."));
            worker.setSalary(salary);

            // Dates
            worker.setStartDate(startDatePicker.getValue());
            worker.setEndDate(endDatePicker.getValue());

            // Status
            worker.setStatus(statusComboBox.getValue());

            // Person
            if (!personHeightField.getText().trim().isEmpty() ||
                    eyeColorComboBox.getValue() != null ||
                    hairColorComboBox.getValue() != null ||
                    nationalityComboBox.getValue() != null) {

                Float height = personHeightField.getText().trim().isEmpty() ? null :
                        Float.parseFloat(personHeightField.getText().trim().replace(",", "."));

                Person person = new Person(
                        height,
                        eyeColorComboBox.getValue(),
                        hairColorComboBox.getValue(),
                        nationalityComboBox.getValue()
                );
                worker.setPerson(person);
            }

            return worker;

        } catch (NumberFormatException e) {
            InputDialogs.showErrorDialog(
                    localization.get("dialog.error"),
                    localization.get("error.invalid.number"),
                    e.getMessage()
            );
            return null;
        } catch (IllegalArgumentException e) {
            InputDialogs.showErrorDialog(
                    localization.get("dialog.error"),
                    localization.get("dialog.error"),
                    e.getMessage()
            );
            return null;
        }
    }
}