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

    public Management(ArrayList<Host> hosts, ArrayList<Guest> guests, ArrayList<Location> locations) {
        this.hosts = hosts;
        this.guests = guests;
        this.locations = locations;
    }

    private String fullName;
    private String mail;
    private int phoneNumber;
    private int age;

    private boolean dataChecker(String phoneNumber, int age) {
        if (phoneNumber.length() != 10) {
            System.out.println("The phone number is invalid");
            return false;
        }

        if (age < 18) {
            System.out.println("You must have at least 18 years old.");
            return false;
        }

        return true;
    }

    public void addHost(String fullName, String mail, String phoneNumber, int age) {
        if (dataChecker(phoneNumber, age)) {
            this.hosts.add(new Host(fullName, mail, phoneNumber, age));
            System.out.println("Host has been added.");
        }
    }

    public void addGuest(String fullName, String mail, String phoneNumber, int age) {
        if (dataChecker(phoneNumber, age)) {
            this.guests.add(new Guest(fullName, mail, phoneNumber, age));
            System.out.println("Host has been added.");
        }
    }

    public void addLocation(String hostFullName, String city, String street, int number, float price, String description) {
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
                System.out.println("Host with this name doesn't exist.");

        } else {
            System.out.println("This city isn't registered in our database.");
        }
    }

    public void bookLocation(int locationId, String guestFullName, String months) {
        boolean locationFound = false, guestFound = false;
        String[] monthsList = months.split(" ");

        for (Location location : this.locations) {
            if (location.getId() == locationId) {
                locationFound = true;

                if (location.checkIfFree(monthsList)) {
                    for (Guest guest : this.guests) {
                        if (guest.getFullName().equals(guestFullName)) {
                            guestFound = true;
                            float totalPrice = location.getPrice() * monthsList.length;
                            guest.addBooking(locationId, months, totalPrice);

                            System.out.println("Booking for user " + guestFullName + " at location " +
                                    location.getId() + " on " + months + " was recorded.");
                        }
                    }
                }
            }
        }

        if (!locationFound)
            System.out.println("Location with this id doesn't exist.");

        if (!guestFound)
            System.out.println("Guest with this name doesn't exist.");
    }

    public void displayLocations() {
        for (Location location : this.locations) {
            for (Host host : this.hosts)
                if (host.getHostedLocations().contains(location)) {
                    System.out.println("Host: " + host.getFullName());
                    System.out.println(location);
                }
        }
    }

    public void displayCityLocations(String city) {
        for (Location location : this.locations) {
            if (location.getCity().equals(city)) {
                for (Host host : this.hosts)
                    if (host.getHostedLocations().contains(location)) {
                        System.out.println("Host: " + host.getFullName());
                        System.out.println(location);
                    }
            }
        }
    }
}
