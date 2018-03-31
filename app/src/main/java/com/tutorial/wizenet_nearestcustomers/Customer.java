package com.tutorial.wizenet_nearestcustomers;

/**
 * Created by Danny on 31/03/2018.
 */

public class Customer {

    private String name;
    private String city;
    private String address;

    public Customer(String name, String city, String address) {
        this.name = name;
        this.city = city;
        this.address = address;
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
}
