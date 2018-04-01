//package com.tutorial.wizenet_nearestcustomers;
//
//import android.content.Context;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.RequiresApi;
//import android.util.Log;
//
//import com.google.android.gms.maps.model.LatLng;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.locks.ReentrantLock;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//
//
//public class GeocodingLocation {
//
//    /**
//     * Private constructor.
//     */
//    private GeocodingLocation() {
//
//    }
//
//    private static final String TAG = "GeocodingLocation";
//    private static Geocoder geocoder = null;
//
//    public static void getAddressFromLocation(final String locationAddress,
//                                              final Context context, final Handler handler) {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//
//                double latitude = 0.0;
//                double longitude = 0.0;
//                String distanceText = null;
//                String distanceValue = null;
//                boolean ok = true;
//
//                try {
//
////                    ReentrantLock mutex = new ReentrantLock();
////
////                    mutex.lock();
////
////                    if (geocoder == null) {
////                        geocoder = new Geocoder(context, Locale.getDefault());
////                    }
////
////                    //TODO either try to send address as is to google maps, or try to run the line below in a recursion up to some counter
////                    List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);
////
////                    mutex.unlock();
////
////                    if (addressList != null && addressList.size() > 0) {
////                        Address address = addressList.get(0);
////                        latitude = address.getLatitude();
////                        longitude = address.getLongitude();
////                    }
//
//                    //TODO delete this
//                    LatLng origin = new LatLng(32.091412, 34.895811);
//                    LatLng destination = new LatLng(latitude, longitude);
//
//                    //Calculate distance from origin to specified destination.
//                    JSONObject distance = calculateDistance(origin, locationAddress);
//
//                    try {
//
//                        //Distance in a readable form(m, km)
//                        distanceText = distance.getString("text");
//
//                        //Distance in meters.
//                        distanceValue = distance.getString("value");
//
//                        // distanceTxt.setText(distanceText);
//
//                    } catch (JSONException | NullPointerException e) {
//                        e.printStackTrace();
//                        ok = false;
//                        // distanceTxt.setText("לא ניתן לחשב מרחק");
//                    }
//
////                }
////                catch (IOException e) {
////                    Log.e(TAG, "Unable to connect to Geocoder", e);
////                    e.printStackTrace();
////
////                    //Specify that an error has occurred.
////                    ok = false;
//
//                } finally {
//
//                    Message message = Message.obtain();
//                    message.setTarget(handler);
//
//                    /*
//                    Note:
//                    message.what = 1: means everything is ok.
//                    message.what = 2: means an error has occurred.
//                     */
//                    if (!ok) {
//                        message.what = 2;
//                    } else {
//                        message.what = 1;
//                    }
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Text", distanceText);
//                    bundle.putString("Value", distanceValue);
//                    message.setData(bundle);
//                    message.sendToTarget();
//                }
//            }
//        };
//        thread.start();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private static JSONObject calculateDistance(final LatLng origin, String rawDestination) {
//
//        final JSONObject[] distance = new JSONObject[1];
//        // final String response;
//
//        final String destination = rawDestination.replaceAll(" ", "+");
//
//        String strUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" +
//                origin.latitude + "," + origin.longitude +
//                "&destination=" + destination +
//                "&sensor=false&units=metric&mode=driving";
//
//
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet httpget= new HttpGet(URL);
//
//            HttpResponse response = httpclient.execute(httpget);
//
//
//            JSONObject jsonObject = new JSONObject(response.toString());
//            JSONArray array = jsonObject.getJSONArray("routes");
//            JSONObject routes = array.getJSONObject(0);
//            JSONArray legs = routes.getJSONArray("legs");
//            JSONObject steps = legs.getJSONObject(0);
//            distance[0] = steps.getJSONObject("distance");
//            // parsedDistance[0] = distance.getString("text");
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        return distance[0];
//    }
//}