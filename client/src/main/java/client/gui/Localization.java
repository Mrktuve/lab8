package client.gui;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Управляет локализацией интерфейса.
 */
public class Localization {

    private ResourceBundle bundle;
    private String currentLocale;

    public Localization() {
        setLocale("ru");
    }

    public void setLocale(String localeCode) {
        this.currentLocale = localeCode;

        Locale locale = switch (localeCode) {
            case "et" -> new Locale("et", "EE");
            case "lt" -> new Locale("lt", "LT");
            case "es_CR" -> new Locale("es", "CR");
            default -> new Locale("ru", "RU");
        };

        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }

    public String get(String key, Object... args) {
        String pattern = get(key);
        return String.format(pattern, args);
    }

    public String getCurrentLocale() {
        return currentLocale;
    }
}