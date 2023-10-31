import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final Server server;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            String s = null;
            try {
                s = receiveChat();
            } catch (IOException e) {
                try {
                    server.terminate(this);
                    clientSocket.close();
                    this.interrupt();
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    return;
                }
            }

            if (s.substring(0, 4).equals("exit")) {
                try {
                    server.deliverChat(s.substring(5) + " leave the conversation.");
                    server.log(s.substring(5) + " leave the conversation.");
                } catch (IOException e) {
                    System.out.println("Cannot send confirmation.");
                }

            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    server.deliverChat("[" + LocalDateTime.now().format(formatter) + "] " + s);
                } catch (IOException e) {
                    System.out.println("Cannot send data.");
                    break;
                }
            }

        }

        close();
    }

    private void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveChat() throws IOException {
        if (!clientSocket.isClosed()) {
            // InputStreamReader inputStreamReader = new
            // InputStreamReader(clientSocket.getInputStream());
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            // BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String rStr = dataInputStream.readUTF();
            return rStr;
        } else {
            throw new IOException("Socket is closed.");
        }

    }

    public void sendChat(String chat) throws IOException {
        if (!clientSocket.isClosed()) {
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            // OutputStreamWriter outputStreamWriter = new
            // OutputStreamWriter(clientSocket.getOutputStream());
            // BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            // bufferedWriter.write(chat);
            dataOutputStream.writeUTF(chat);
        } else {
            System.out.println("Socket is closed.");
        }
    }
}
