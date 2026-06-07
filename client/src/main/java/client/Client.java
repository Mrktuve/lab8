package client;

import client.gui.GuiBootstrap;
import javafx.application.Application;

/**
 * Точка входа клиентского приложения для работы с коллекцией работников.
 */
public class Client {

    /**
     * Запускает графический интерфейс клиента на базе JavaFX.
     *
     * @param args аргументы командной строки, передаваемые в {@link Application}
     */
    public static void main(String[] args) {
        Application.launch(GuiBootstrap.class, args);
    }
}