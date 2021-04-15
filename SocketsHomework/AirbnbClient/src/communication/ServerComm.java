package communication;

import java.io.IOException;
import java.util.ArrayList;

public class ServerComm {
    private final ArrayList<Client> clients;

    public ServerComm() throws IOException {
        this.clients = new ArrayList<>();
    }

    public void addClient(String clientName) throws IOException {
        this.clients.add(new Client(clientName));
    }

    public void communicate() throws IOException {
        this.messageEnvelope(this.clients.get(0), "invalid message");
        this.messageEnvelope(this.clients.get(0), "add_host Host1 host1_name@yahoo.com 0772047293 33");
        this.messageEnvelope(this.clients.get(0), "add_guest Guest1 guest1_name@yahoo.com 0712639201 22");
        this.messageEnvelope(this.clients.get(0), "add_guest Guest2 guest2_name@yahoo.com 0753585257 42");
        this.messageEnvelope(this.clients.get(0), "add_host Host2 host2_name@yahoo.com 0729389183 39");
        this.messageEnvelope(this.clients.get(0), "add_host GuestInvalid guest_invalid_name@yahoo.com 0753989257 12");
        this.messageEnvelope(this.clients.get(1), "add_location Host1 Buzau Pasteur 35 150.0 invalid_location");
        this.messageEnvelope(this.clients.get(1), "add_location Host1 Cluj-Napoca Pasteur 14 450.0 quiet_location");
        this.messageEnvelope(this.clients.get(1), "add_location Host1 Cluj-Napoca Observatorului 182 575.0 friendly_location");
        this.messageEnvelope(this.clients.get(4), "add_location Host2 Brasov Victoriei 8 850.0 deluxe_location");
        this.messageEnvelope(this.clients.get(2), "show_available_locations");
        this.messageEnvelope(this.clients.get(3), "show_available_city_locations Cluj-Napoca");
        this.messageEnvelope(this.clients.get(2), "book_location 1 Guest1 June_July_August");
        this.messageEnvelope(this.clients.get(2), "show_available_city_locations Cluj-Napoca");
        this.messageEnvelope(this.clients.get(3), "book_location 1 Guest2 May_June");
        this.messageEnvelope(this.clients.get(3), "book_location 2 Guest2 December");
        this.messageEnvelope(this.clients.get(3), "show_available_locations");

        this.closeCommunication();
    }

    public void messageEnvelope(Client client, String message) throws IOException {
        System.out.println("\nFor server by " + client.getClientName() + ":\n" + message);
        client.getForServer().println(message);
        message = client.getFromServer().readLine();
        System.out.println("From server:");

        if (message.contains("    "))
            System.out.println(message.replace("    ", "\n"));
        else
            System.out.println(message);
    }

    private void closeCommunication() throws IOException {
        for (Client client : this.clients) {
            client.getForServer().println("Close communication with " + client.getClientName() + ".");
            client.closeClientCommunication();
        }
    }
}
