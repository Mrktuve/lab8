package client.gui;

import client.NetworkClient;
import common.commands.*;
import common.enums.Status;
import common.model.Worker;
import common.network.Request;
import common.network.Response;
import java.util.List;

public class ClientService {
    private final NetworkClient networkClient;
    private final Session session;
    private final Localization localization;

    public ClientService(NetworkClient networkClient, Session session, Localization localization) {
        this.networkClient = networkClient;
        this.session = session;
        this.localization = localization;
    }

    public Response login(String login, String password) {
        try {
            Request request = new Request(new Login(), login, password);
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response register(String login, String password) {
        try {
            Request request = new Request(new Register(), login, password);
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Worker> loadCollection() {
        try {
            Request request = new Request(new Show(), session.getLogin(), session.getPassword());
            Response response = networkClient.sendRequest(request);
            if (response.isSuccess() && response.getData() != null) {
                return (List<Worker>) response.getData();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ИСПРАВЛЕНО: передаем worker в конструктор Add, Request принимает 3 аргумента
    public Response addWorker(Worker worker) {
        try {
            Request request = new Request(new Add(worker), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response addIfMax(Worker worker) {
        try {
            Request request = new Request(new AddIfMax(worker), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response updateWorker(long id, Worker worker) {
        try {
            Request request = new Request(new UpdateId(id, worker), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    // ИСПРАВЛЕНО: RemoveById вместо RemoveId
    public Response removeById(long id) {
        try {
            Request request = new Request(new RemoveById(id), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    // ДОБАВЛЕНО: removeLower
    public Response removeLower(Worker worker) {
        try {
            Request request = new Request(new RemoveLower(worker), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    // ДОБАВЛЕНО: removeAnyByStatus
    public Response removeAnyByStatus(Status status) {
        try {
            Request request = new Request(new RemoveAnyByStatus(status), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response clearCollection() {
        try {
            Request request = new Request(new Clear(), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    // ИСПРАВЛЕНО: убран бесполезный параметр id
    public Response info() {
        try {
            Request request = new Request(new Info(), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response help() {
        try {
            Request request = new Request(new Help(), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    // ДОБАВЛЕНО: history
    public Response history() {
        try {
            Request request = new Request(new History(), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    // ДОБАВЛЕНО: printDescending
    public Response printDescending() {
        try {
            Request request = new Request(new PrintDescending(), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    // ДОБАВЛЕНО: filterStartsWithName
    public Response filterStartsWithName(String prefix) {
        try {
            Request request = new Request(new FilterStartsWithName(prefix), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Session getSession() { return session; }
}