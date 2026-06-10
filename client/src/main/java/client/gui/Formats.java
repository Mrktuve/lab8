package client.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Утилиты для форматирования дат и чисел.
 */
public class Formats {

    private static final DateTimeFormatter DATE_FORMATTER_RU =
            DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru"));
    private static final DateTimeFormatter DATE_FORMATTER_ET =
            DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("et"));
    private static final DateTimeFormatter DATE_FORMATTER_LT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("lt"));
    private static final DateTimeFormatter DATE_FORMATTER_ES =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("es", "CR"));

    /**
     * Форматирует дату согласно локали.
     */
    public static String formatDate(LocalDate date, String locale) {
        if (date == null) return "";

        DateTimeFormatter formatter = switch (locale) {
            case "et" -> DATE_FORMATTER_ET;
            case "lt" -> DATE_FORMATTER_LT;
            case "es_CR" -> DATE_FORMATTER_ES;
            default -> DATE_FORMATTER_RU;
        };

        return date.format(formatter);
    }

    /**
     * Форматирует число (зарплату) с разделителями.
     */
    public static String formatNumber(Number number, String locale) {
        if (number == null) return "";

        Locale loc = switch (locale) {
            case "et" -> new Locale("et", "EE");
            case "lt" -> new Locale("lt", "LT");
            case "es_CR" -> new Locale("es", "CR");
            default -> new Locale("ru", "RU");
        };

        return String.format(loc, "%,." + (number instanceof Double || number instanceof Float ? "2f" : "d"), number);
    }

    /**
     * Парсит строку в LocalDate.
     */
    public static LocalDate parseDate(String dateString, String locale) {
        if (dateString == null || dateString.trim().isEmpty()) return null;

        DateTimeFormatter formatter = switch (locale) {
            case "et" -> DATE_FORMATTER_ET;
            case "lt" -> DATE_FORMATTER_LT;
            case "es_CR" -> DATE_FORMATTER_ES;
            default -> DATE_FORMATTER_RU;
        };

        try {
            return LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}