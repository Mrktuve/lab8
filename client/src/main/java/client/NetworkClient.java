package client;

import common.network.Request;
import common.network.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Менеджер TCP-соединения клиента с сервером: отправка запросов и получение ответов.
 */
public class NetworkClient implements AutoCloseable {

    /** Таймаут ожидания ответа от сервера, мс */
    private static final int TIMEOUT = 10000;

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean connected = false;

    /**
     * Создаёт менеджер и устанавливает соединение с сервером по указанному хосту и порту.
     *
     * @param host адрес сервера
     * @param port порт сервера
     * @throws IOException при ошибке открытия соединения
     */
    public NetworkClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        connect();
    }

    /**
     * Открывает TCP-соединение с сервером.
     *
     * @throws IOException при ошибке создания или настройки соединения
     */
    public void connect() throws IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(TIMEOUT);

        // Создаём только выходной поток (ObjectOutputStream отправляет заголовок автоматически)
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        // ObjectInputStream создаём лениво - при первом чтении
        // Это предотвращает deadlock с сервером

        connected = true;
        System.out.println("Connected to " + host + ":" + port);
    }

    /**
     * Инициализирует входной поток при необходимости.
     */
    private void ensureInputStream() throws IOException {
        if (in == null && socket != null) {
            in = new ObjectInputStream(socket.getInputStream());
        }
    }

    /**
     * Отправляет запрос на сервер и ожидает ответ.
     *
     * @param request запрос к серверу
     * @return ответ сервера
     * @throws IOException при ошибке сетевого взаимодействия
     */
    public Response sendRequest(Request request) throws IOException {
        try {
            // Отправляем запрос
            out.writeObject(request);
            out.flush();

            // Инициализируем входной поток при необходимости
            ensureInputStream();

            // Читаем ответ
            Response response = (Response) in.readObject();
            return response;

        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to deserialize response", e);
        }
    }

    /**
     * Проверяет, установлено ли соединение с сервером.
     *
     * @return true если соединение активно
     */
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }

    /**
     * Закрывает TCP-соединение с сервером.
     */
    @Override
    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Closed connection to " + host + ":" + port);
            }
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}