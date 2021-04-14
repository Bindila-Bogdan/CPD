package model;

import java.util.ArrayList;

public class Host extends User {
    private ArrayList<Location> hostedLocations;

    public Host(String fullName, String mail, String phoneNumber, int age) {
        super(fullName, mail, phoneNumber, age);
        this.hostedLocations = new ArrayList<>();
    }

    public void addHostedLocation(Location location){
        this.hostedLocations.add(location);
    }

    public ArrayList<Location> getHostedLocations() {
        return hostedLocations;
    }

    public void setHostedLocations(ArrayList<Location> hostedLocations) {
        this.hostedLocations = hostedLocations;
    }
}