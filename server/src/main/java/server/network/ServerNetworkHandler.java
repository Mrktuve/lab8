package server.network;

import common.network.Request;
import common.network.Response;
import server.core.CommandExecutor;
import server.database.DatabaseManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerNetworkHandler {
    private final int port;
    private final CommandExecutor commandExecutor;
    private final ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private final ExecutorService pool = Executors.newCachedThreadPool();

    

    public ServerNetworkHandler(int port, CommandExecutor executor) {
        this.port = port;
        this.commandExecutor = executor;

    }

    public void start() {
        try {


            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started on port " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    ClientHandler handler = new ClientHandler(clientSocket, commandExecutor);
                    String key = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                    clients.put(key, handler);

                    pool.submit(handler);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final CommandExecutor executor;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public ClientHandler(Socket socket, CommandExecutor executor) {
            this.socket = socket;
            this.executor = executor;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

                System.out.println("Streams initialized for " + socket.getInetAddress());

                while (true) {
                    Request request = (Request) in.readObject();
                    if (request == null) break;

                    System.out.println("Received request: " + request.getCommand());

                    Response response = executor.execute(request);
                    System.out.println("Sending response: " + response.isSuccess());

                    out.writeObject(response);
                    out.flush();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Client disconnected: " + socket.getInetAddress());
            } finally {
                cleanup();
            }
        }

        private void cleanup() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();

                String key = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                clients.remove(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}