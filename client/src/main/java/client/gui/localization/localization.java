package client.gui.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class localization {

    private Locale locale;
    private ResourceBundle bundle;

    public localization() {
        setLocale("ru");
    }

    public void setLocale(String languageCode) {

        switch (languageCode) {

            case "et":
                locale = new Locale("et");
                break;

            case "lt":
                locale = new Locale("lt");
                break;

            case "es_CR":
                locale = new Locale("es", "CR");
                break;

            default:
                locale = new Locale("ru");
                break;
        }

        bundle = ResourceBundle.getBundle(
                "client.gui.localization.Resources",
                locale
        );
    }

    public String get(String key) {

        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public String getCurrentLocale() {

        if (locale.getLanguage().equals("es")) {
            return "es_CR";
        }

        return locale.getLanguage();
    }
}