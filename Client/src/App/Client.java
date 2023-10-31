package App;

import java.io.IOException;
import java.net.Socket;

import DataTransfer.DataTransfer;
import View.ClientGUI;

public class Client {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    private void start() {
        try {
            String serverIP = "127.0.0.1";
            int serverPort = 2626;

            Socket socket = new Socket(serverIP, serverPort);
            DataTransfer dt = new DataTransfer(socket);

            new ClientGUI(dt, this);
        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    public static void main(String[] args) throws Exception {
        new Client().start();
    }
}
