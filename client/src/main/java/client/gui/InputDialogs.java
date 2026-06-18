package client.gui;

import javafx.scene.control.*;


/**
 * Утилитарные методы для создания диалоговых окон ввода.
 */
public class InputDialogs {

    /**
     * Показывает диалог выбора из списка.
     */
    public static <T> T showChoiceDialog(String title, String header, String content, java.util.List<T> choices) {
        ChoiceDialog<T> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait().orElse(null);
    }

    /**
     * Показывает диалог подтверждения.
     */
    public static boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait().filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    /**
     * Показывает диалог с информацией.
     */
    public static void showInfoDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Показывает диалог с ошибкой.
     */
    public static void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}