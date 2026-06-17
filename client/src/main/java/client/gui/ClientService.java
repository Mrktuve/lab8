package client.gui;

import client.NetworkClient;
import client.gui.localization.localization;
import common.commands.*;
import common.enums.Status;
import common.model.Worker;
import common.network.Request;
import common.network.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClientService {
    private final NetworkClient networkClient;
    private final Session session;
    private final localization localization;

    public ClientService(NetworkClient networkClient, Session session, localization localization) {
        this.networkClient = networkClient;
        this.session = session;
        this.localization = localization;
    }


    @SuppressWarnings("unchecked")
    public List<Worker> loadCollection() {
        try {

            Request request = new Request(new Show(), session.getLogin(), session.getPassword());
            Response response = networkClient.sendRequest(request);

            if (response.isSuccess() && response.getData() != null) {
                return new ArrayList<>((Collection<Worker>) response.getData());
            }
            return List.of();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


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


    public Response removeById(long id) {
        try {
            Request request = new Request(new RemoveById(id), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }


    public Response removeLower(Worker worker) {
        try {
            Request request = new Request(new RemoveLower(worker), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }


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


    public Response history() {
        try {
            Request request = new Request(new History(), session.getLogin(), session.getPassword());
            return networkClient.sendRequest(request);
        } catch (Exception e) {
            return new Response(false, localization.get("error.network") + ": " + e.getMessage());
        }
    }


    public Session getSession() {
        return session;
    }
}