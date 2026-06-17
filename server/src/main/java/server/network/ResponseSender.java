package server.network;

import common.network.Response;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseSender {
    private final ConcurrentHashMap<String, ObjectOutputStream> streams = new ConcurrentHashMap<>();

    public void send(Socket socket, Response response) {
        try {
            String key = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
            ObjectOutputStream out = streams.get(key);

            if (out == null) {
                out = new ObjectOutputStream(socket.getOutputStream());
                streams.put(key, out);
            }

            out.writeObject(response);
            out.flush();
        } catch (Exception e) {
            System.err.println("[ResponseSender] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void remove(Socket socket) {
        String key = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        streams.remove(key);
    }
}