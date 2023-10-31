import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

    public Set<ClientHandler> clientHandlers = new HashSet<ClientHandler>();
    private int port = 2626;
    ServerSocket serverSocket;
    ServerGUI serverGUI;

    public int getPortNumber() {
        return this.port;
    }

    void openGUI() throws Exception {
        serverGUI = new ServerGUI(this);
    }

    public void start() throws Exception {
        serverSocket = new ServerSocket(port);

        while (!serverSocket.isClosed()) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientThread = new ClientHandler(clientSocket, this);
            clientHandlers.add(clientThread);
            clientThread.start();
            Thread.sleep(500);
        }

        serverSocket.close();
    }

    public void stop() throws Exception {
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
        serverGUI.log(s);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
    }
}
