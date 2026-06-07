package client.gui;

import client.NetworkClient;
import common.network.Request;
import common.network.Response;
import common.Worker;

import java.io.IOException;
import java.util.List;

/**
 * Сервис для взаимодействия с сервером.
 * Отвечает за формирование запросов, отправку их через NetworkClient
 * и обработку полученных ответов.
 */
public class ClientService {

    private final NetworkClient networkClient;
    private final Session session;
    private final Localization localization;

    public ClientService(NetworkClient networkClient, Session session, Localization localization) {
        this.networkClient = networkClient;
        this.session = session;
        this.localization = localization;
    }

    /**
     * Универсальный метод отправки команды на сервер.
     *
     * @param commandName имя команды (например, "show", "add", "clear")
     * @param argument    аргумент команды (строка, число или объект Worker, может быть null)
     * @return Response от сервера
     * @throws IOException при ошибке сети
     */
    public Response executeCommand(String commandName, Object argument) throws IOException {
        // Формируем запрос.
        // ВАЖНО: Убедись, что конструктор твоего Request принимает (command, login, password, argument)
        // Если у тебя используется токен или только login, адаптируй эту строку.
        Request request = new Request(commandName, session.getLogin(), session.getPassword(), argument);
        return networkClient.sendRequest(request);
    }

    /**
     * Загружает коллекцию работников с сервера (команда "show").
     *
     * @return список работников, или null в случае ошибки
     */
    @SuppressWarnings("unchecked")
    public List<Worker> loadCollection() {
        try {
            Response response = executeCommand("show", null);
            if (response.isSuccess() && response.getData() != null) {
                // Сервер должен возвращать коллекцию в поле data
                return (List<Worker>) response.getData();
            } else {
                System.err.println("Ошибка загрузки коллекции: " + response.getMessage());
                return null;
            }
        } catch (IOException e) {
            System.err.println("Сетевая ошибка при загрузке коллекции: " + e.getMessage());
            return null;
        }
    }

    /**
     * Добавляет нового работника (команда "add").
     */
    public Response addWorker(Worker worker) {
        try {
            return executeCommand("add", worker);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage(), null);
        }
    }

    /**
     * Добавляет работника, если его значение больше максимального (команда "add_if_max").
     */
    public Response addIfMax(Worker worker) {
        try {
            return executeCommand("add_if_max", worker);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage(), null);
        }
    }

    /**
     * Обновляет существующего работника по ID (команда "update").
     */
    public Response updateWorker(long id, Worker worker) {
        try {
            // Аргументом может быть массив или специальный объект, зависит от твоего сервера.
            // Часто делают: аргумент = id (String), а объект Worker передается отдельно,
            // или аргумент = сам Worker, у которого уже проставлен ID.
            // Здесь предполагаем, что аргумент - это ID в виде String, а Worker - это argument (если Request поддерживает 2 аргумента)
            // ЛИБО (более стандартно для Лабы 8): аргументом является ID, а сервер берет Worker из... стоп.
            // Стандартный вариант Лабы 8: команда "update id {element}".
            // Если твой Request принимает только один Object argument, передай туда Worker, а ID установи внутри него,
            // либо создай Map/Record.
            // Давай сделаем так: аргумент - это ID, но тогда как передать Worker?
            // Обычно делают: new Request("update", login, pass, id) и сервер ждет Worker в отдельном поле,
            // ИЛИ аргумент - это сам Worker с уже установленным ID.

            worker.setId(id); // Устанавливаем ID в объекте
            return executeCommand("update", worker);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage(), null);
        }
    }

    /**
     * Удаляет элемент по ID (команда "remove_by_id").
     */
    public Response removeById(long id) {
        try {
            return executeCommand("remove_by_id", String.valueOf(id));
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage(), null);
        }
    }

    /**
     * Очищает коллекцию (команда "clear").
     */
    public Response clearCollection() {
        try {
            return executeCommand("clear", null);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage(), null);
        }
    }

    /**
     * Выполняет скрипт из файла (команда "execute_script").
     * Примечание: Сам файл читается на клиенте, и команды отправляются по одной,
     * либо сервер сам читает файл (зависит от ТЗ). Если клиент читает, этот метод может не понадобиться в таком виде.
     */
    public Response executeScript(String filePath) {
        try {
            return executeCommand("execute_script", filePath);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage(), null);
        }
    }
}