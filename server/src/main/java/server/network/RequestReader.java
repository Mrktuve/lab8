package server.network;

import common.network.Request;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class RequestReader {
    private final ConcurrentHashMap<String, ObjectInputStream> streams = new ConcurrentHashMap<>();

    public Request read(Socket socket) {
        try {
            String key = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
            ObjectInputStream in = streams.get(key);


            if (in == null) {

                new java.io.ObjectOutputStream(socket.getOutputStream()).flush();
                in = new ObjectInputStream(socket.getInputStream());
                streams.put(key, in);
            }

            return (Request) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void remove(Socket socket) {
        String key = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        streams.remove(key);
    }
}