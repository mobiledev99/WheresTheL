package com.mobiledev.wheresthel;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Activity used to display an L stop detail page in a fragment
 *
 * An LStop object is passed to the fragment for display
 *
 * @author Terry Chaisson
 * @version %I%, %G%
 *
 */

public class LStopActivity extends SingleFragmentActivity {

    private LStop lstop;
    private LStopSearchTerms lStopSearchTerms;
    private String mode;
    public static final String MAP_DIRECTIONS_ACTION = "google.navigation:q=";
    public static final String MAP_MAPS_ACTION = "geo:";
    public static final String MAP_NO_LOCATION_LAT_LONG = "0,0";
    public static final String MAP_PARM = "?q=";
    public static final String MAP_DIRECTIONS_MODE = "&mode=";
    public static final String GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps";
    public static final String GOOGLE_MAPS_CLASSNAME = "com.google.android.maps.MapsActivity";

    /**
     * Gets an LStop object from the calling intent and returns a fragment
     * containing the LStop object for display
     *
     * @return a new fragment with the Lstop object to be displayed
     */

    @Override
    protected Fragment createFragment() {
        lstop = (LStop) getIntent().getSerializableExtra(LStopFragment.LSTOP);
        lStopSearchTerms = (LStopSearchTerms) getIntent().getSerializableExtra(LStopSearchTerms.LSTOPSEARCHTERMS);

        return LStopFragment.newInstance(lstop, lStopSearchTerms);
    }

    public void btnGetDirectionsClicked(View v) {

        openModeAlert(v);
        Log.i(this.getTitle().toString(), "Mode= " + mode);
        if(mode != null){
            getDirections();
        }
    }

    public void btnGetMapStationClicked(View v) {
            getMap();
    }

    private void openModeAlert (View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(R.string.transportation_method);
        alertDialogBuilder.setMessage(R.string.transportation_mode_message);
        // set positive button: Driving d
        alertDialogBuilder.setPositiveButton(R.string.mode_driving, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mode = "d";
                dialog.cancel();
            }
        });
        // set negative button: Walking w
        alertDialogBuilder.setNegativeButton(R.string.mode_walking, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mode = "w";
                dialog.cancel();
            }
        });
        // set neutral button: Bicycling b
        alertDialogBuilder.setNeutralButton(R.string.mode_bicycling, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mode = "b";
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void getDirections() {
        String uri = MAP_DIRECTIONS_ACTION + lstop.getLatitude() + "," + lstop.getLongitude()+
                MAP_DIRECTIONS_MODE + mode;
        Uri gmmIntentURI = Uri.parse(uri);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW, gmmIntentURI);
        directionsIntent.setClassName(GOOGLE_MAPS_PACKAGE, GOOGLE_MAPS_CLASSNAME);
        if(directionsIntent.resolveActivity((getPackageManager())) != null) {
            startActivity(directionsIntent);
        }
        else {
            Toast.makeText(this, getString(R.string.could_not_get_directions),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getMap() {

        String urlString = lstop.getStation_name().replace(" ", "+");

/*        String uri = MAP_MAPS_ACTION + MAP_NO_LOCATION_LAT_LONG + MAP_PARM +
                lstop.getLatitude() + "," + lstop.getLongitude()+ "(" +
                urlString + ")";
  */
        String uri = MAP_MAPS_ACTION + lStopSearchTerms.getLatitude() +
                "," + lStopSearchTerms.getLongitude() + MAP_PARM +
                lstop.getLatitude() + "," + lstop.getLongitude()+ "(" +
                urlString + ")";

        Uri gmmIntentURI = Uri.parse(uri);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW, gmmIntentURI);
        directionsIntent.setClassName(GOOGLE_MAPS_PACKAGE, GOOGLE_MAPS_CLASSNAME);
        if(directionsIntent.resolveActivity((getPackageManager())) != null) {
            startActivity(directionsIntent);
        }
        else {
            Toast.makeText(this, getString(R.string.could_not_get_map),
                    Toast.LENGTH_LONG).show();
        }
    }

}
