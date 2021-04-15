package communication;

import business.Management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommHandler extends Thread {
    private final Socket clientSocket;
    private final BufferedReader fromClient;
    private final PrintWriter forClient;
    private final Management management;

    public CommHandler(Socket clientSocket, Management management) throws IOException {
        this.clientSocket = clientSocket;
        fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        forClient = new PrintWriter(clientSocket.getOutputStream(), true);
        this.management = management;
    }

    @Override
    public void run() {
        try {
            String message;
            while (((message = fromClient.readLine()) != null)) {
                if (message.contains("Close communication")) {
                    System.out.println(message);
                    this.closeCommunication();
                    break;
                } else {
                    message = this.processRequest(message);
                    forClient.println(message);
                    System.out.println("For client:\n" + message.replace("    ", "\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCommunication() throws IOException {
        this.fromClient.close();
        this.forClient.close();
        clientSocket.close();
    }

    private String processRequest(String receivedMessage) {
        System.out.println("\nFrom client:\n" + receivedMessage);
        String[] message = receivedMessage.split(" ");
        String messageToSend;

        switch (message[0]) {
            case "add_guest":
                messageToSend = management.addGuest(message[1], message[2], message[3], Integer.parseInt(message[4]));
                break;
            case "add_host":
                messageToSend = management.addHost(message[1], message[2], message[3], Integer.parseInt(message[4]));
                break;
            case "add_location":
                messageToSend = management.addLocation(message[1], message[2], message[3], Integer.parseInt(message[4]),
                        Float.parseFloat(message[5]), message[6]);
                break;
            case "show_available_locations":
                messageToSend = management.displayLocations();
                break;
            case "show_available_city_locations":
                messageToSend = management.displayCityLocations(message[1]);
                break;
            case "book_location":
                messageToSend = management.bookLocation(Integer.parseInt(message[1]), message[2], message[3]);
                break;
            default:
                messageToSend = "Invalid request. Valid requests are: {add_guest, add_host, add_location," +
                        " show_available_locations, show_available_city_locations, book_location}.";
                break;
        }

        return messageToSend;
    }
}
