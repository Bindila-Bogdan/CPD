package communication;

import business.Management;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientComm {
    private final int PORT = 8;
    private final ServerSocket serverSocket;
    private final Management management;

    public ClientComm() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.management = new Management();
    }

    public void handleRequests() throws IOException {
        while (true) {
            Socket clientSocket = this.serverSocket.accept();
            CommHandler commHandler = new CommHandler(clientSocket, management);
            commHandler.start();
        }
    }
}
