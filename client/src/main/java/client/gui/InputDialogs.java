package client.gui;

import client.gui.localization.localization;
import javafx.scene.control.*;

import java.time.LocalDate;

/**
 * Утилитарные методы для создания диалоговых окон ввода.
 */
public class InputDialogs {

    /**
     * Показывает диалог ввода строки.
     */
    public static String showInputDialog(String title, String header, String content, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait().orElse(null);
    }

    /**
     * Показывает диалог ввода числа (Long).
     */
    public static Long showLongInputDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        return dialog.showAndWait()
                .flatMap(s -> {
                    try {
                        return java.util.Optional.of(Long.parseLong(s.trim()));
                    } catch (NumberFormatException e) {
                        return java.util.Optional.empty();
                    }
                })
                .orElse(null);
    }

    /**
     * Показывает диалог ввода числа (Double).
     */
    public static Double showDoubleInputDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        return dialog.showAndWait()
                .flatMap(s -> {
                    try {
                        return java.util.Optional.of(Double.parseDouble(s.trim().replace(",", ".")));
                    } catch (NumberFormatException e) {
                        return java.util.Optional.empty();
                    }
                })
                .orElse(null);
    }

    /**
     * Показывает диалог ввода даты.
     */
    public static LocalDate showDateInputDialog(String title, String header, String content, localization loc) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content + " (YYYY-MM-DD)");

        return dialog.showAndWait()
                .flatMap(s -> {
                    try {
                        return java.util.Optional.of(LocalDate.parse(s.trim()));
                    } catch (Exception e) {
                        return java.util.Optional.empty();
                    }
                })
                .orElse(null);
    }

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