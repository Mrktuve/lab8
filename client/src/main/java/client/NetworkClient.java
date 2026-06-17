package client;

import common.network.Request;
import common.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkClient implements AutoCloseable {

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Response sendRequest(Request request) {
        try {
            // Если соединение не установлено - создаём новое
            if (socket == null || socket.isClosed()) {
                connect();
            }

            // Отправляем запрос
            out.writeObject(request);
            out.flush();

            // Читаем ответ
            return (Response) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            // При ошибке закрываем соединение
            close();
            return new Response(false, "Connection error: " + e.getMessage());
        }
    }

    private void connect() throws IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(10000);

        // ВАЖНО: Создаём потоки в правильном порядке
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}