package com.tutorial.wizenet_nearestcustomers;

/**
 * Created by Danny on 31/03/2018.
 */

public class Customer {

    private String name;
    private String city;
    private String address;
    private String distanceToUserText;
    private double distanceToUserValue;

    public Customer(String name, String city, String address) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.distanceToUserText = null;
        this.distanceToUserValue = 0.0;

    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getDistanceToUserText() {
        return distanceToUserText;
    }

    public double getDistanceToUserValue() {
        return distanceToUserValue;
    }

    public void setDistanceToUserValue(double distanceToUserValue) {
        this.distanceToUserValue = distanceToUserValue;
    }

    public void setDistanceToUserText(String distanceToUserText) {
        this.distanceToUserText = distanceToUserText;
    }
}
