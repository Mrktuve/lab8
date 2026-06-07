package client.gui;

import client.NetworkClient;
import common.model.*;
import common.network.Request;
import common.network.Response;
import common.commands.*;
import common.enums.*;
import java.io.IOException;
import java.util.List;

/**
 * Сервис для взаимодействия с сервером.
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
     * Загружает коллекцию работников с сервера.
     */
    @SuppressWarnings("unchecked")
    public List<Worker> loadCollection() {
        try {
            System.out.println("[ClientService] Loading collection...");
            Command showCommand = new Show();
            Request request = new Request(showCommand, session.getLogin(), session.getPassword());

            System.out.println("[ClientService] Sending request: " + request);
            Response response = networkClient.sendRequest(request);

            System.out.println("[ClientService] Response received: " + response);
            System.out.println("[ClientService] Response success: " + response.isSuccess());
            System.out.println("[ClientService] Response data: " + (response.getData() != null ? "NOT NULL" : "NULL"));

            if (response.isSuccess() && response.getData() != null) {
                List<Worker> workers = (List<Worker>) response.getData();
                System.out.println("[ClientService] Loaded " + workers.size() + " workers");
                return workers;
            } else {
                System.err.println("[ClientService] Load failed: " + response.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.err.println("[ClientService] Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Добавляет нового работника.
     */
    public Response addWorker(Worker worker) {
        try {
            Command addCommand = new Add(worker);
            Request request = new Request(addCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    /**
     * Добавляет работника, если его значение больше максимального.
     */
    public Response addIfMax(Worker worker) {
        try {
            Command addIfMaxCommand = new AddIfMax(worker);
            Request request = new Request(addIfMaxCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    /**
     * Обновляет работника по ID.
     */
    public Response updateWorker(long id, Worker worker) {
        try {
            // Используем UpdateId вместо Update
            Command updateCommand = new UpdateId(id, worker);
            Request request = new Request(updateCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    /**
     * Удаляет по ID.
     */
    public Response removeById(long id) {
        try {
            Command removeCommand = new RemoveById(id);
            Request request = new Request(removeCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    /**
     * Очищает коллекцию.
     */
    public Response clearCollection() {
        try {
            Command clearCommand = new Clear();
            Request request = new Request(clearCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    /**
     * Получает информацию о коллекции.
     */
    public Response info() {
        try {
            Command infoCommand = new Info();
            Request request = new Request(infoCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    /**
     * Фильтрует по началу имени.
     */
    public Response filterStartsWithName(String name) {
        try {
            Command filterCommand = new FilterStartsWithName(name);
            Request request = new Request(filterCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (IOException e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

}