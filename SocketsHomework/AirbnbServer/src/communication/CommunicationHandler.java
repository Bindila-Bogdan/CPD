package communication;

import business.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader fromClient;
    private PrintWriter forClient;
    private String message;
    private Management management;

    public CommunicationHandler(Socket clientSocket, Management management) throws IOException {
        this.clientSocket = clientSocket;
        fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        forClient = new PrintWriter(clientSocket.getOutputStream(), true);
        this.management = management;
    }

    @Override
    public void run() {
        try {
            while (((message = fromClient.readLine()) != null)) {
                if (message.equals("close communication"))
                    break;
                else {
                    System.out.println(message);
                    forClient.println("Hello client!");
                }
            }
            this.closeCommunication();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCommunication() throws IOException {
        System.out.println("Closing the communication.");

        this.fromClient.close();
        this.forClient.close();
        clientSocket.close();
    }
}
