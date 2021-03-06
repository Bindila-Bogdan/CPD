package business;

import model.Guest;
import model.Host;
import model.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class Management {
    private int maxLocationId = -1;
    private final ArrayList<String> CITIES = new ArrayList<>(Arrays.asList("Cluj-Napoca", "Brasov", "Galati",
            "Iasi", "Timisoara", "Constanta"));
    private final ArrayList<Host> hosts;
    private final ArrayList<Guest> guests;
    private final ArrayList<Location> locations;

    public Management() {
        this.hosts = new ArrayList<Host>();
        this.guests = new ArrayList<Guest>();
        this.locations = new ArrayList<Location>();
    }

    private String dataChecker(String phoneNumber, int age) {
        String message = "";

        if (phoneNumber.length() != 10) {
            message = "The phone number is invalid";
            return message;
        }

        if (age < 18) {
            message = "You must have at least 18 years old.";
            return message;
        }

        return message;
    }

    public String addHost(String fullName, String mail, String phoneNumber, int age) {
        String message = dataChecker(phoneNumber, age);

        if (message.length() == 0) {
            this.hosts.add(new Host(fullName, mail, phoneNumber, age));
            message = "Host " + fullName + " has been added.";

        }
        return message;
    }

    public String addGuest(String fullName, String mail, String phoneNumber, int age) {
        String message = dataChecker(phoneNumber, age);

        if (message.length() == 0) {
            this.guests.add(new Guest(fullName, mail, phoneNumber, age));
            message = "Guest " + fullName + " has been added.";

        }
        return message;
    }

    public String addLocation(String hostFullName, String city, String street, int number, float price, String description) {
        String message = "Location was added successfully.";
        boolean hostFound = false;

        if (this.CITIES.contains(city)) {
            for (Host host : this.hosts) {
                if (host.getFullName().equals(hostFullName)) {
                    hostFound = true;
                    maxLocationId++;
                    Location location = new Location(maxLocationId, city, street, number, price, description);
                    this.locations.add(location);
                    host.addHostedLocation(location);
                }
            }

            if (!hostFound)
                message = "Host with this name doesn't exist.";

        } else
            message = "This city isn't registered in our database.";

        return message;
    }

    public String bookLocation(int locationId, String guestFullName, String months) {
        boolean locationFound = false, guestFound = false;
        String[] monthsList = months.split("_");
        StringBuilder message = new StringBuilder();

        for (Location location : this.locations) {
            if (location.getId() == locationId) {
                locationFound = true;
                message = new StringBuilder(location.checkIfFree(monthsList));
                if (location.checkIfFree(monthsList).contains("is free")) {
                    for (Guest guest : this.guests) {
                        if (guest.getFullName().equals(guestFullName)) {
                            guestFound = true;
                            float totalPrice = location.getPrice() * monthsList.length;
                            guest.addBooking(locationId, months, totalPrice);
                            location.bookForMonths(monthsList, guestFullName);
                            message.append("Booking for user ").append(guestFullName).append(" at location ")
                                    .append(location.getId()).append(" on ").append(months).append(" was recorded.")
                                    .append("The price is: ").append(location.getPrice() * monthsList.length).append(" lei.");
                        }
                    }
                } else
                    return message.toString();
            }
        }

        if (!locationFound)
            message = new StringBuilder("Location with this id doesn't exist.");

        if (!guestFound)
            message = new StringBuilder("Guest with this name doesn't exist.");

        return message.toString();
    }

    public String displayLocations() {
        StringBuilder message = new StringBuilder();

        for (Location location : this.locations) {
            for (Host host : this.hosts)
                if (host.getHostedLocations().contains(location)) {
                    message.append("    Host: ").append(host.getFullName());
                    message.append(location);
                }
        }

        return message.toString();
    }

    public String displayCityLocations(String city) {
        StringBuilder message = new StringBuilder();

        for (Location location : this.locations) {
            if (location.getCity().equals(city)) {
                for (Host host : this.hosts)
                    if (host.getHostedLocations().contains(location)) {
                        message.append("    Host: ").append(host.getFullName());
                        message.append(location);
                    }
            }
        }

        return message.toString();
    }
}
