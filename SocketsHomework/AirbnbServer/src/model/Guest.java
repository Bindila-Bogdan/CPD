package model;

import java.util.HashMap;

public class Guest extends User {
    private HashMap<Integer, String> bookingHistory;

    public Guest(String fullName, String mail, String phoneNumber, int age) {
        super(fullName, mail, phoneNumber, age);
        this.bookingHistory = new HashMap<Integer, String>();
    }

    public void addBooking(Integer locationId, String months, float totalPrice) {
        this.bookingHistory.put(locationId, "price: " + totalPrice + " months: " + months);
    }

    public HashMap<Integer, String> getBookingHistory() {
        return bookingHistory;
    }

    public void setBookingHistory(HashMap<Integer, String> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void showBookingsHistory() {
        for (Integer locationId : this.bookingHistory.keySet()) {
            System.out.println("Location " + locationId.toString() +
                    " was booked for months " + this.bookingHistory.get(locationId));
        }
    }
}
