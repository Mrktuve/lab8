package server.network;

import common.network.Response;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class ResponseSender {

    public void send(Socket socket, Response response) {

        try {
            // Используем существующий поток или создаём новый
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            out.writeObject(response);
            out.flush();
        } catch (Exception e) {
            System.err.println("[ResponseWriter] Error writing response: " + e.getMessage());
            e.printStackTrace();
        }
    }
}