package com.tutorial.wizenet_nearestcustomers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Danny on 31/03/2018.
 */

public class DistancesListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Customer> customers;
//    private LatLng origin;

    public DistancesListAdapter(Context context, ArrayList<Customer> customers) {

        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.customers = customers;
        //this.origin = origin;
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

        //Get current customer.
        Customer currentCustomer = this.customers.get(i);

        //Set customer values into the row.
        customerName.setText(currentCustomer.getName());
        city.setText(currentCustomer.getCity());
        address.setText(currentCustomer.getAddress());
        distance.setText(currentCustomer.getDistanceToUserText());

//        //Perform distance calculation.
//        new CustomGetHttp(new GeocoderHandler(origin, distance));

//        //TODO delete this
//        while(distance.getText() == "מרחק"){}
        //GeocodingLocation.getAddressFromLocation(fullAddress, context, new GeocoderHandler(origin, distance));

        return view;
    }

//    private class GeocoderHandler extends Handler {
//
//        private LatLng origin;
//        private LatLng destination;
//        private TextView distanceTextView;
//
//        public GeocoderHandler(LatLng origin, TextView distanceTxt) {
//
//            this.origin = origin;
//            this.destination = null;
//            this.distanceTextView = distanceTxt;
//        }
//
//        @Override
//        public void handleMessage(Message message) {
//            //String locationAddress;
//            String distanceText = null;
//            String distanceValue = null;
//            switch (message.what) {
//                case 1:
//                    Bundle bundle = message.getData();
//                    distanceText = bundle.getString("Text");
//                    distanceValue = bundle.getString("Value");
//                    //locationAddress = String.format("%f %f", latitude, longitude);
//                    break;
//                case 2:
//                    distanceText = null;
//                    break;
//                default:
//                    distanceText = null;
//            }
//
//            if (distanceText == null) {
//              //  distanceTextView.setText("לא ניתן לחשב מרחק");
//            } else {
//                distanceTextView.setText(distanceText);
//            }
//        }
//    }
}
