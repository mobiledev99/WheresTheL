package com.mobiledev.wheresthel;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Terry on 5/2/2015.
 *
 * LStopListFragment displays a list of L stops
 *
 * @author Terry Chaisson
 * @version %I%, %G%
 */
public class LStopListFragment extends ListFragment {

    public static final String LSTOPDATABASE = "LSTOPDATABASE";
    private LstopDatabase lstopDatabase;
    private LStopSearchTerms lStopSearchTerms;

    private ArrayList<LStop> mLStops;

    private static final String TAG = "LStopListFragment";
    private GeoLocation myLocation;
    private double earthRadius = 6371.01;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //lstopDatabase = (LstopDatabase) getArguments().getSerializable(LSTOPDATABASE);
        lStopSearchTerms = (LStopSearchTerms) getArguments().getSerializable(LStopSearchTerms.LSTOPSEARCHTERMS);

        // Gets a reference to the activity and set the title of the page
        getActivity().setTitle(R.string.title_activity_lstops);

        myLocation = GeoLocation.fromDegrees(Double.parseDouble(lStopSearchTerms.getLatitude()),
                Double.parseDouble(lStopSearchTerms.getLongitude()));

        // Get distance - convert to kilometers
        double distance = Double.parseDouble(lStopSearchTerms.getDistance()) * 1.60934;

        GeoLocation[] boundingCoordinates = myLocation.boundingCoordinates(distance, earthRadius);
        boolean meridian180WithinDistance= boundingCoordinates[0].getLongitudeInDegrees() >
                boundingCoordinates[1].getLongitudeInDegrees();

        LStopQueryValues lStopQueryValues = new LStopQueryValues();

        lStopQueryValues.setCity(lStopSearchTerms.getCity());

        lStopQueryValues.setDirection(lStopSearchTerms.getDirection());

        String orAnd = (meridian180WithinDistance ? "OR" : "AND");

        lStopQueryValues.setOrAnd(orAnd);

        lStopQueryValues.setMinLat(boundingCoordinates[0].getLatitudeInDegrees());
        lStopQueryValues.setMaxLat(boundingCoordinates[1].getLatitudeInDegrees());
        lStopQueryValues.setMinLon(boundingCoordinates[0].getLongitudeInDegrees());
        lStopQueryValues.setMaxLon(boundingCoordinates[1].getLongitudeInDegrees());
        lStopQueryValues.setSinLat(myLocation.getLatitudeInDegrees());
        lStopQueryValues.setCosLat(myLocation.getLatitudeInDegrees());
        lStopQueryValues.setCosLon(myLocation.getLongitudeInDegrees());

        double angularRadius = new BigDecimal(distance/ earthRadius).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();

        lStopQueryValues.setAngularRadius(angularRadius);


        new FetchLstopsTask().execute(lStopQueryValues);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        /* Get the selected LStop object from the list
            Setup an intent with the LStop object to be passed in
            start the intent to list the detail page
        */
        LStop lStop = ((LStopAdapter)getListAdapter()).getItem(position);
        Intent lStopsIntent = new Intent(getActivity(),LStopActivity.class);
        lStopsIntent.putExtra(LStopFragment.LSTOP, lStop);
        lStopsIntent.putExtra(LStopSearchTerms.LSTOPSEARCHTERMS, lStopSearchTerms);
        startActivity(lStopsIntent);

    }

    /**
     * Setup a custom data source for the list view in order to display information from
     * LStop objects
     */
    private class LStopAdapter extends ArrayAdapter<LStop> {
        public LStopAdapter(ArrayList<LStop> lstops) {
            super(getActivity(), 0, lstops);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            /* Reuse content views if available
                else get a new content view
                get an LStop item from the data source (pointed to by position)
                Setup custom views with data from the LStop object
                and return the custom view for display in the list view
              */
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_lstop, null);
            }

            LStop l = getItem(position);

            TextView tvStationName = (TextView) convertView.findViewById(R.id.lstop_list_item_stationName);
            tvStationName.setText(l.getStation_name());

            TextView tvDirection = (TextView) convertView.findViewById(R.id.lstop_list_item_direction);
            tvDirection.setText(l.getDirection_id());

            TextView tvStationDescription = (TextView) convertView.findViewById(R.id.lstop_list_item_stationDescriptiveName);
            tvStationDescription.setText(l.getStation_descriptive_name());

            TextView tvDistance = (TextView) convertView.findViewById(R.id.lstop_list_item_distance);

            double distance = new BigDecimal(myLocation.distanceTo(GeoLocation.
                    fromDegrees(Double.parseDouble(l.getLatitude()),
                            Double.parseDouble(l.getLongitude())), earthRadius)).
                    setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();

            tvDistance.setText(Double.toString(distance));

            return convertView;
        }
    }

    /**
     * Create a new instance of an LStopFragment and place an LStop object in
     * the bundle to be passed to LStopActivity for display
     * @param lStopSearchTerms object to pass to the activity
     * @return a new instance of an LStopFragment
     */
    public static LStopListFragment newInstance(LStopSearchTerms lStopSearchTerms) {

        Bundle args = new Bundle();
        //args.putSerializable(LSTOPDATABASE, lstopDatabase);
        args.putSerializable(LStopSearchTerms.LSTOPSEARCHTERMS, lStopSearchTerms);

        LStopListFragment fragment = new LStopListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private class FetchLstopsTask extends AsyncTask<LStopQueryValues, Void, ArrayList<LStop>> {


        @Override
        protected ArrayList<LStop> doInBackground(LStopQueryValues... params) {

            // Gets a list of LStop objects from the LStopFactory for test data
            // TODO: get the LStop data from a database
            LstopDatabase lStopDB = new LstopDatabase(getActivity());

            ArrayList<LStop> lStops = new ArrayList<LStop>();

            lStops = LStopFactory.get(getActivity()).fetchLStopsWithinDistance(params[0]);
            try {
            //    lStops = lStopDB.fetchLStopsWithinDistance(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Log.i(TAG, "LStopQueryValues: City - " + params[0].getCity() + " Direction -  "
                    + params[0].getDirection());

            return lStops;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<LStop> aLStops) {
            mLStops = aLStops;
            // Set the data source for the list view
            LStopAdapter adapter = new LStopAdapter(mLStops);
            setListAdapter(adapter);

            Log.i(TAG, "Made it to onPostExecute");
        }


    }
}
