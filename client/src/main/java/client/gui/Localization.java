package client.gui;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Управляет локализацией интерфейса.
 * Поддерживает языки: русский, эстонский, литовский, испанский (Коста-Рика).
 */
public class Localization {

    private ResourceBundle bundle;
    private String currentLocale;

    public Localization() {
        setLocale("ru");
    }

    /**
     * Устанавливает локаль по коду языка.
     *
     * @param localeCode код языка: "ru", "et", "lt", "es_CR"
     */
    public void setLocale(String localeCode) {
        this.currentLocale = localeCode;

        Locale locale = switch (localeCode) {
            case "et" -> new Locale("et", "EE");
            case "lt" -> new Locale("lt", "LT");
            case "es_CR" -> new Locale("es", "CR");
            default -> new Locale("ru", "RU");
        };

        // Загружаем ресурсный файл messages_{locale}.properties
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Возвращает строку по ключу для текущей локали.
     */
    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }

    /**
     * Возвращает строку по ключу с параметрами.
     */
    public String get(String key, Object... args) {
        String pattern = get(key);
        return String.format(pattern, args);
    }

    /**
     * Возвращает текущий код локали.
     */
    public String getCurrentLocale() {
        return currentLocale;
    }
}