package com.mobiledev.wheresthel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Terry on 5/1/2015.
 *
 * Fragment used to display an LStop object on a detail page
 *
 * mapStation and getDirections buttons will display a map
 * of the L station location and get directions to the station, respectively.
 *
 * To be implemented later
 *
 *
 * @author Terry Chaisson
 * @version %I%, %G%
 *
 */
public class LStopFragment extends Fragment {
    public static final String LSTOP = "LSTOP";
    private LStop lStop;
    private TextView direction;
    private TextView stopName;
    private TextView stationName;
    private TextView stationDescription;
    private Button mapStation;
    private Button getDirections;
    private LStopSearchTerms lStopSearchTerms;



    /**
     * Retrieves an LStop object from the bundle passed in when the fragment is created
     *
     * @param savedInstanceState bundle that contains the LStop object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lStop = (LStop) getArguments().getSerializable(LSTOP);
        lStopSearchTerms = (LStopSearchTerms) getArguments().getSerializable(LStopSearchTerms.LSTOPSEARCHTERMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view

        View v = inflater.inflate(R.layout.fragment_lstop, container, false);

        // Get references to text views
        direction = (TextView) v.findViewById(R.id.tvDirection);
        stopName = (TextView) v.findViewById(R.id.tvStopName);
        stationName = (TextView) v.findViewById(R.id.tvStationName);
        stationDescription = (TextView) v.findViewById(R.id.tvStationDescription);

        // Set text view text to values passed in on the LStop object
        direction.setText(lStop.getDirection_id());
        stopName.setText(lStop.getStop_name());
        stationName.setText(lStop.getStation_name());
        stationDescription.setText(lStop.getStation_descriptive_name());

        // Return the view with the content to be displayed
        return v;
    }

    /**
     * Create a new instance of an LStopFragment and place an LStop object in
     * the bundle to be passed to LStopActivity for display
     * @param lstop object to pass to the activity
     * @return a new instance of an LStopFragment
     */
    public static LStopFragment newInstance(LStop lstop, LStopSearchTerms lStopSearchTerms) {

        Bundle args = new Bundle();
        args.putSerializable(LSTOP, lstop);
        args.putSerializable(LStopSearchTerms.LSTOPSEARCHTERMS, lStopSearchTerms);

        LStopFragment fragment = new LStopFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
