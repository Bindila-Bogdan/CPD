package communication;

import business.Management;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Communication {
    private final int PORT = 8;
    private ServerSocket serverSocket;
    private Management management;

    public Communication() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.management = new Management();
    }

    public void handleRequests() throws IOException {
        while(true){
            Socket clientSocket = this.serverSocket.accept();
            CommunicationHandler communicationHandler = new CommunicationHandler(clientSocket, management);
            communicationHandler.start();
        }
    }
}
