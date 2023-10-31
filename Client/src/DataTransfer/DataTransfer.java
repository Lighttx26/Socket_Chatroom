package DataTransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataTransfer {

    public final Socket socket;
    private boolean isclose;

    public DataTransfer(Socket socket) {
        this.socket = socket;
        isclose = false;
    }

    public void send(String str) {
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            // outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            // bufferedWriter = new BufferedWriter(outputStreamWriter);
        } catch (IOException e) {
            System.err.println("Cannot get OutputStream from connection.");
        }

        try {
            dataOutputStream.writeUTF(str);
            // bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println("Cannot send data to server.");
        }
    }

    public String receive() {
        DataInputStream dataInputStream = null;
        // InputStreamReader inputStreamReader = null;
        // BufferedReader bufferedReader = null;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Cannot get InputStream from connection.");
        }

        String respond = null;
        try {
            if (socket.isConnected()) {
                respond = dataInputStream.readUTF();
                // respond = bufferedReader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Cannot read data.");
            e.printStackTrace();
        }

        return respond;
    }

    public void close() {
        if (!(this.socket.isClosed())) {
            try {
                isclose = true;
                this.socket.close();
            } catch (IOException e) {
                System.err.println("Cannot close connection.");
                e.printStackTrace();
            }
        }
    }

    public boolean isClose() {
        return isclose;
    }
}
