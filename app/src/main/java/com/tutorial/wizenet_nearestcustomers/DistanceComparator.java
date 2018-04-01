package com.tutorial.wizenet_nearestcustomers;

import java.util.Comparator;

/**
 * Created by Danny on 01/04/2018.
 */

public class DistanceComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer customer1, Customer customer2) {

        if(customer1.getDistanceToUserValue() < customer2.getDistanceToUserValue()){
            return -1;
        }
        else if(customer1.getDistanceToUserValue() > customer2.getDistanceToUserValue()){
            return 1;
        }

        return 0;
    }
}
