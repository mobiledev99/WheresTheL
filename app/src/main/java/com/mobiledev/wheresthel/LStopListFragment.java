package com.mobiledev.wheresthel;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private ArrayList<LStop> mLStops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gets a reference to the activity and set the title of the page
        getActivity().setTitle(R.string.title_activity_lstops);

        // Gets a list of LStop objects from the LStopFactory for test data
        // TODO: get the LStop data from a database
        mLStops = LStopFactory.get(getActivity()).getLStops();

        // Set the data source for the list view
        LStopAdapter adapter = new LStopAdapter(mLStops);
        setListAdapter(adapter);
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

            return convertView;
        }
    }
}
