package com.tutorial.wizenet_nearestcustomers;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Customer> customers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
          TODO if the large code is not working to convert address to LatLong, use:
          Geocoder gc = new Geocoder(context);
         if(gc.isPresent()){
         List<Address> list = gc.getFromLocationName(“155 Park Theater, Palo Alto, CA”, 1);
         Address address = list.get(0);
         double lat = address.getLatitude();
         double lng = address.getLongitude();
         */

        ListView distancesListView = (ListView) findViewById(R.id.customers_distances_listView);

        //Initializes the customers list.
        initializeCustomers();

        //TODO The location values of the user, should be supplied from outside.
        LatLng origin = new LatLng(32.091412, 34.895811);

        DistancesListAdapter adapter = new DistancesListAdapter(this, this.customers, origin);

        distancesListView.setAdapter(adapter);

    }

    private void initializeCustomers(){

        this.customers = new ArrayList<>();
        this.customers.add(new Customer("משה כהן", "פתח תקווה", "חיים עוזר 1"));
        this.customers.add(new Customer("אבי כהו", "פתח תקווה", "רוטשילד 8 "));
        this.customers.add(new Customer("אאא", "תל אביב", "אלנבי 1"));
        this.customers.add(new Customer("בבב", "תל אביב", "דיזינגוף 5"));
        this.customers.add(new Customer("גגג", "נתניה", "הרצל 1"));
        this.customers.add(new Customer("דדד", "אילת", "התמרים 1"));
        this.customers.add(new Customer("ההה", "אילת", "התמרים 10"));
    }
}
