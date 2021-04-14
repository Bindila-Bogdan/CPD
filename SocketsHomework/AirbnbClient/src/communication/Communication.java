package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Communication {
    private final String IP = "127.0.0.1";
    private final int PORT = 8;
    private final Socket socket;

    private BufferedReader fromServer;
    private PrintWriter forServer;
    private String message;

    public Communication() throws IOException {
        this.socket = new Socket(IP, PORT);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        forServer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void communicate() throws IOException {
        forServer.println("Hello server!");
        message = fromServer.readLine();
        System.out.println(message);
        this.closeCommunication();
    }

    private void closeCommunication() throws IOException {
        System.out.println("Closing the communication.");

        this.fromServer.close();
        this.forServer.close();
        socket.close();
    }
}
