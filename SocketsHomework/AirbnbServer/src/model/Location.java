package model;

import java.util.Arrays;
import java.util.HashMap;

public class Location {
    private int id;
    private String city;
    private String street;
    private int number;
    private float price;
    private String description;
    private HashMap<String, String> bookings;

    public Location(int id, String city, String street, int number, float price, String description) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.number = number;
        this.price = price;
        this.description = description;
        this.bookings = new HashMap<>() {{
            put("January", null);
            put("February", null);
            put("March", null);
            put("April", null);
            put("May", null);
            put("June", null);
            put("July", null);
            put("August", null);
            put("September", null);
            put("October", null);
            put("November", null);
            put("December", null);
        }};
    }

    public boolean checkIfFree(String[] months) {
        boolean free = true;
        String bookedMonths = "";

        for (String month : months) {
            if (this.bookings.get(month) != null) {
                free = false;
                bookedMonths += month;
            }
        }

        if (free)
            System.out.println("Location is free on " + Arrays.toString(months) + ".");
        else
            System.out.println("This location is booked on " + bookedMonths + ".");

        return free;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<String, String> getBookings() {
        return bookings;
    }

    public void setBookings(HashMap<String, String> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        String locationInfo = new String("id: " + this.id +
                " city: " + this.city + " street: " + this.street +
                " number: " + number + " price: " + price +
                " description: " + description + " available months: ");

        for (String month : this.bookings.keySet()) {
            if (this.bookings.get(month) == null)
                locationInfo += (month + " ");
        }

        return locationInfo;
    }
}
