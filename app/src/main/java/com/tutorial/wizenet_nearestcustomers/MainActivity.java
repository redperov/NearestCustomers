package com.tutorial.wizenet_nearestcustomers;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements IObserver {

    private ListView distancesListView;
    private DistancesListAdapter adapter;
    private SeekBar seekBar;
    private TextView distanceText;

    //Customers list.
    private ArrayList<CustomerTmp> customers;

    //Counter used in the handler to count the number of tasks that were finished.
    private static int distancesCalculatedCounter;

    //Initial seek bar cursor value.
    private final int INITIAL_PROGRESS = 10;

    //API key used to address the Google Maps server.
    private final String API_KEY = "AIzaSyANBZWaAFBg_iSdpjkcapK3PZOi4ZtvXmI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO IMPORTANT: before using this app: need to provide current user location and create an API key.


        this.seekBar = (SeekBar) findViewById(R.id.customers_distances_seekBar);
        this.distancesListView = (ListView) findViewById(R.id.customers_distances_listView);
        this.distanceText = (TextView) findViewById(R.id.customers_distances_distanceText);

        //Sets the initial position of the seek bar cursor.
        this.seekBar.setProgress(INITIAL_PROGRESS);

        //Initializes the customers list.
        initializeCustomers();

        //TODO The location values of the user, should be supplied from outside.
        LatLng origin = new LatLng(32.091412, 34.895811);

        //Calculate distances from the user to the customers.
        boolean hasSucceeded = calculateDistancesToUser(origin);

        if (!hasSucceeded) {
            Toast.makeText(this, "Failed to create all asyncTasks", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean calculateDistancesToUser(LatLng origin) {

        for (CustomerTmp customer : this.customers) {

            String destination = customer.getAddress() + customer.getCity();

            try {
                String encodedDestination = URLEncoder.encode(destination, "UTF-8");


                String strUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                        origin.latitude + "," + origin.longitude +
                        "&destination=" + encodedDestination +
                        "&sensor=false&units=metric&mode=driving&key=" + API_KEY;

                //Perform distance calculation.
                new GetDistanceHttp(new GeocoderHandler(this, origin, customer)).execute(strUrl);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

                return false;
            }
        }

        return true;
    }

    private void initializeCustomers() {

        this.customers = new ArrayList<>();
        this.customers.add(new CustomerTmp("דדד", "אילת", "התמרים 1"));
        this.customers.add(new CustomerTmp("משה כהן", "פתח תקווה", "חיים עוזר 1"));
        this.customers.add(new CustomerTmp("בבב", "תל אביב", "דיזינגוף 5"));
        this.customers.add(new CustomerTmp("ההה", "אילת", "התמרים 10"));
        this.customers.add(new CustomerTmp("אאא", "תל אביב", "אלנבי 1"));
        this.customers.add(new CustomerTmp("גגג", "נתניה", "הרצל 1"));
        this.customers.add(new CustomerTmp("אבי כהן", "פתח תקווה", "הרצל 8"));

        distancesCalculatedCounter = this.customers.size();
    }

    @Override
    public void update() {

        Collections.sort(this.customers, new DistanceComparator());

        adapter = new DistancesListAdapter(this, customers);

        distancesListView.setAdapter(adapter);

        this.distanceText.setText(this.seekBar.getProgress() + " km");
        adapter.getFilter().filter(Integer.toString(INITIAL_PROGRESS));
        adapter.setCustomers(customers);

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressValue;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                progressValue = i;
                distanceText.setText(progressValue + " km");
                adapter.getFilter().filter(Integer.toString(progressValue));
                adapter.setCustomers(customers);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                distanceText.setText(progressValue + " km");
               // adapter.getFilter().filter(Integer.toString(progressValue));
            }
        });

    }

    private class GeocoderHandler extends Handler implements IObservable {

        private LatLng origin;
        private LatLng destination;
        private CustomerTmp customer;
        private IObserver observer;

        public GeocoderHandler(IObserver observer, LatLng origin, CustomerTmp customer) {

            this.observer = observer;
            this.origin = origin;
            this.destination = null;
            this.customer = customer;
        }

        @Override
        public void handleMessage(Message message) {
            //String locationAddress;
            String distanceText = null;
            double distanceValue = 0.0;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    distanceText = bundle.getString("Text");
                    distanceValue = bundle.getDouble("Value");
                    //locationAddress = String.format("%f %f", latitude, longitude);
                    break;
                case 2:
                    distanceText = null;
                    break;
                default:
                    distanceText = null;
            }

            if (distanceText == null) {
                this.customer.setDistanceToUserText("שגיאה");
                //  distanceTextView.setText("לא ניתן לחשב מרחק");
            } else {
//                distanceTextView.setText(distanceText);
                this.customer.setDistanceToUserText(distanceText);
                this.customer.setDistanceToUserValue(distanceValue);
            }

            distancesCalculatedCounter--;

            if (distancesCalculatedCounter == 0) {
                this.notifyObservers();
            }
        }

        @Override
        public void notifyObservers() {
            observer.update();
        }
    }
}
