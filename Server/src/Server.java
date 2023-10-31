import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Server extends Thread {

    public Set<ClientHandler> clientHandlers = new HashSet<ClientHandler>();
    private int port = 2626;
    ServerSocket serverSocket;
    ServerGUI serverGUI;

    public Server() {
        super();
    }

    public Server(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    public int getPortNumber() {
        return this.port;
    }

    public int getPortNumber2() {
        return this.serverSocket.getLocalPort();
    }

    void openGUI() {
        serverGUI = new ServerGUI(this);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket, this);
                clientHandlers.add(clientThread);
                clientThread.start();
                Thread.sleep(500);
            }
        } catch (IOException e1) {
            System.out.println("Server socket is closed.\n");
        } catch (InterruptedException e2) {
            System.out.println("Thread cannot sleep: " + e2.getMessage() + "\n");
        }

    }

    public void stopServer() throws Exception {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.interrupt();
        }

        clientHandlers.clear();

        if (!serverSocket.isClosed())
            serverSocket.close();
    }

    public void deliverChat(String s) throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendChat(s);
        }
    }

    public void terminate(ClientHandler clientHandler) throws Exception {
        clientHandlers.remove(clientHandler);
    }

    public void log(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        serverGUI.log("[" + LocalDateTime.now().format(formatter) + "] " + s);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.openGUI();
    }
}
