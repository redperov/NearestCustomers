package com.tutorial.wizenet_nearestcustomers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Danny on 31/03/2018.
 */

public class CustomGetHttp extends AsyncTask<String , Void ,String> {
    private String serverResponse;
    private Handler handler;

    public CustomGetHttp(Handler handler){
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                serverResponse = readStream(urlConnection.getInputStream());
            }
            else{
                Log.e("myTag", "Http error, status code" + responseCode);
                serverResponse = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        callHandler();
    }

    private void callHandler(){

        Message message = Message.obtain();
        message.setTarget(handler);

        //Setting the message to ok for now.
        message.what = 1;
        JSONObject distanceJson = null;

        try{
            JSONObject jsonObject = new JSONObject(serverResponse.toString());
            JSONArray array = jsonObject.getJSONArray("routes");
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
            distanceJson = steps.getJSONObject("distance");

            //Distance in a readable form(m, km)
            String distanceText = distanceJson.getString("text");

            //Distance in meters.
            double distanceValue = Double.parseDouble(distanceJson.getString("value"));

            Bundle bundle = new Bundle();
            bundle.putString("Text", distanceText);
            bundle.putDouble("Value", distanceValue);
            message.setData(bundle);
        }
        catch(NullPointerException | JSONException e){

            e.printStackTrace();

            Log.e("myTag", "Server response error " + serverResponse);

            //Setting that an error has occurred.
            message.what = 2;
        }

        message.sendToTarget();
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
