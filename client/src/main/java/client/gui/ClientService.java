package client.gui;


import client.NetworkClient;
import common.commands.*;
import common.network.Request;
import common.network.Response;

import common.model.Worker;

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
            Command loginCommand = new Login();
            Request request = new Request(loginCommand, login, password);
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response register(String login, String password) {
        try {
            Command registerCommand = new Register();
            Request request = new Request(registerCommand, login, password);
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Worker> loadCollection() {
        try {
            Command showCommand = new Show();
            Request request = new Request(showCommand, session.getLogin(), session.getPassword());
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

    public Response addWorker(Worker worker) {
        try {
            Command addCommand = new Add();
            Request request = new Request(addCommand, worker, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response addIfMax(Worker worker) {
        try {
            Command addIfMaxCommand = new AddIfMax(worker);
            Request request = new Request(addIfMaxCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response updateWorker(long id, Worker worker) {
        try {
            Command updateCommand = new UpdateId(id, worker);
            Request request = new Request(updateCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response removeWorker(long id) {
        try {
            Command removeCommand = new RemoveId(id);
            Request request = new Request(removeCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response clearCollection() {
        try {
            Command clearCommand = new Clear();
            Request request = new Request(clearCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response getInfo(long id) {
        try {
            Command infoCommand = new Info();
            Request request = new Request(infoCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    public Response help() {
        try {
            Command helpCommand = new Help();
            Request request = new Request(helpCommand, session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }

    
}