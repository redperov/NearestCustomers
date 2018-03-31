package com.tutorial.wizenet_nearestcustomers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Danny on 31/03/2018.
 */

public class DistancesListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Customer> customers;
    private LatLng origin;

    public DistancesListAdapter(Context context, ArrayList<Customer> customers, LatLng origin) {

        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.customers = customers;
        this.origin = origin;
        //this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return this.customers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {

            view = this.layoutInflater.inflate(R.layout.customers_distances_row, viewGroup, false);
        }

        //For each row in the list, add the following items.
        TextView customerName = view.findViewById(R.id.customers_distances_row_name);
        TextView distance = view.findViewById(R.id.customers_distances_row_distance);
        TextView city = view.findViewById(R.id.customers_distances_row_city);
        TextView address = view.findViewById(R.id.customers_distances_row_address);
        String fullAddress;

        //Get current customer.
        Customer currentCustomer = this.customers.get(i);

        //Set customer values into the row.
        customerName.setText(currentCustomer.getName());
        city.setText(currentCustomer.getCity());
        address.setText(currentCustomer.getAddress());

        fullAddress = String.format("%s %s", currentCustomer.getAddress(), currentCustomer.getCity());

        //Perform distance calculation.
        GeocodingLocation.getAddressFromLocation(fullAddress, context, new GeocoderHandler(origin, distance));

        return view;
    }

    private class GeocoderHandler extends Handler {

        private LatLng origin;
        private LatLng destination;
        private TextView distanceTxt;

        public GeocoderHandler(LatLng origin, TextView distanceTxt) {

            this.origin = origin;
            this.destination = null;
            this.distanceTxt = distanceTxt;
        }

        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            double latitude = 0.0;
            double longitude = 0.0;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    latitude = bundle.getDouble("Latitude");
                    longitude = bundle.getDouble("Longitude");
                    locationAddress = String.format("%f %f", latitude, longitude);
                    break;
                case 2:
                    locationAddress = null;
                    break;
                default:
                    locationAddress = null;
            }

            if (locationAddress == null) {
                distanceTxt.setText("לא ניתן לחשב מרחק");
            } else {

                this.destination = new LatLng(latitude, longitude);

                //Calculate distance from origin to specified destination.
                JSONObject distance = calculateDistance(this.origin, this.destination);

                try {

                    //Distance in a readable form(m, km)
                    String distanceText = distance.getString("text");

                    //Distance in meters.
                    String distanceValue = distance.getString("value");

                    distanceTxt.setText(distanceText);

                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();

                   // distanceTxt.setText("לא ניתן לחשב מרחק");
                }
            }
        }

        private JSONObject calculateDistance(final LatLng origin, final LatLng destination) {

            final JSONObject[] distance = new JSONObject[1];
            // final String response;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String strUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" +
                                origin.latitude + "," + origin.longitude +
                                "&destination=" + destination.latitude + "," + destination.longitude +
                                "&sensor=false&units=metric&mode=driving";

                        URL url = new URL(strUrl);
                        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));

                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONArray array = jsonObject.getJSONArray("routes");
                        JSONObject routes = array.getJSONObject(0);
                        JSONArray legs = routes.getJSONArray("legs");
                        JSONObject steps = legs.getJSONObject(0);
                        distance[0] = steps.getJSONObject("distance");
                        // parsedDistance[0] = distance.getString("text");
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return distance[0];
        }
    }
}
