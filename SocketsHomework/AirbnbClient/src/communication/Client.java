package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final String IP = "127.0.0.1";
    private final int PORT = 8;
    private final Socket socket;
    private final String clientName;

    private BufferedReader fromServer;
    private PrintWriter forServer;

    public Client(String clientName) throws IOException {
        this.socket = new Socket(IP, PORT);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        forServer = new PrintWriter(socket.getOutputStream(), true);
        this.clientName = clientName;
    }

    void closeClientCommunication() throws IOException {
        System.out.println(this.clientName + " is closing the communication.");

        this.fromServer.close();
        this.forServer.close();
        socket.close();
    }

    public BufferedReader getFromServer() {
        return fromServer;
    }

    public PrintWriter getForServer() {
        return forServer;
    }

    public String getClientName() {
        return clientName;
    }
}
