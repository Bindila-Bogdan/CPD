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
                    message = this.processRequest(message);
                    forClient.println(message);
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

    private String processRequest(String receivedMessage) {
        System.out.println("\nFrom client:\n" + receivedMessage);
        String[] message = receivedMessage.split(" ");
        String messageToSend = "";

        if (message[0].equals("add_guest"))
            messageToSend = management.addGuest(message[1], message[2], message[3], Integer.parseInt(message[4]));
        else if (message[0].equals("add_host"))
            messageToSend = management.addHost(message[1], message[2], message[3], Integer.parseInt(message[4]));
        else if (message[0].equals("add_location"))
            messageToSend = management.addLocation(message[1], message[2], message[3], Integer.parseInt(message[4]),
                    Float.parseFloat(message[5]), message[6]);
         else if (message[0].equals("show_available_locations"))
            messageToSend = management.displayLocations();
         else if (message[0].equals("show_available_city_locations"))
            messageToSend = management.displayCityLocations(message[1]);
         else if (message[0].equals("book_location"))
            messageToSend = management.bookLocation(Integer.parseInt(message[1]), message[2], message[3]);
         else
            messageToSend = "Invalid request. Valid requests are: {add_guest, add_host, add_location," +
                    " show_available_locations, show_available_city_locations, book_location}";

        System.out.println("For client:\n" + messageToSend);

        return messageToSend;
    }
}
