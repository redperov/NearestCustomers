package com.tutorial.wizenet_nearestcustomers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {

    /**
     * Private constructor.
     */
    private GeocodingLocation() {

    }

    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                double latitude = 0.0;
                double longitude = 0.0;
                boolean ok = true;

                try {
                    List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);

                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        latitude = address.getLatitude();
                        longitude = address.getLongitude();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                    e.printStackTrace();

                    //Specify that an error has occurred.
                    ok = false;

                } finally {

                    Message message = Message.obtain();
                    message.setTarget(handler);

                    /*
                    Note:
                    message.what  = 1: means everything is ok.
                    message.what  = 2: means an error has occurred.
                     */
                    if (!ok) {
                        message.what = 2;
                    } else {
                        message.what = 1;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putDouble("Latitude", latitude);
                    bundle.putDouble("Longitude", longitude);
                    message.setData(bundle);
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}