package server.network;

import common.network.Request;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestReader {

    public Request read(Socket socket) {
        try {
            // ВАЖНО: Сначала создаём выходной поток!
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            // Теперь входной
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            return (Request) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}