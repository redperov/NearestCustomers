package com.tutorial.wizenet_nearestcustomers;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView text;

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

        this.text = (TextView) findViewById(R.id.textView);

        String address = "1600 Pennsylvania Ave NW Washington DC 20502";

        GeocodingLocation.getAddressFromLocation(address,
                getApplicationContext(), new GeocoderHandler());
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    double latitude = bundle.getDouble("Latitude");
                    double longtitude = bundle.getDouble("Longitude");
                    locationAddress = String.format("%f %f", latitude, longtitude);
                    break;
                case 2:
                    locationAddress = "An error has occurred.";
                    break;
                default:
                    locationAddress = null;
            }
            text.setText(locationAddress);
        }
    }
}
