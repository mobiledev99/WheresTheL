package com.mobiledev.wheresthel;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * MainActivity displays the main selection page used to find a list of L stops based on
 * travel direction and a selected distance from the user
 * * @author Terry Chaisson
 * @version %I%, %G%
 *
 */
public class MainActivity extends WheresTheLActivity implements AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RadioGroup rgDirectionGroup;
    private RadioButton rbNorth, rbSouth, rbEast, rbWest;
    private Spinner spnDistance;

    private static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;

    private static int UPDATGE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    public String direction;
    public String distance;

    private LstopDatabase lStopDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RadioGroup used to group selections of mutually exclusive choices for travel direction
        rgDirectionGroup = (RadioGroup) findViewById(R.id.rgDirectionGroup);

        rbNorth = (RadioButton) findViewById(R.id.rbNorth);
        rbSouth = (RadioButton) findViewById(R.id.rbSouth);
        rbEast = (RadioButton) findViewById(R.id.rbEast);
        rbWest = (RadioButton) findViewById(R.id.rbWest);

        // Spinner for choosing a pre-defined distance from the user to L stops
        spnDistance = (Spinner) findViewById(R.id.spnDistance);


        rgDirectionGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                direction = null;

                // Find which radio button was selected
                // Set the direction accordingly
                switch (checkedId) {
                    case R.id.rbNorth:
                        direction = rbNorth.getText().toString();
                        break;
                    case R.id.rbSouth:
                        direction = rbSouth.getText().toString();
                        break;
                    case R.id.rbEast:
                        direction = rbEast.getText().toString();
                        break;
                    case R.id.rbWest:
                        direction = rbWest.getText().toString();
                        break;
                    default:
                        break;
                }

                /* If both direction and distance have been chosen, execute the listLStops
                    method to list the L stops that are within the selected distance with trains
                    that are going in the selected direction
                 */
                if (direction != null && distance !=null) {
                    listLStops();
                }
            }
        });

        spnDistance.setOnItemSelectedListener(this);

        //lStopDB = new LstopDatabase(this);

        if(checkPlayServices()) {

            buildGoogleApiClient();
            createLocationRequest();
        }

    }

    @Override
    public int getCurrentMenuId() {
        return R.id.mainactivity;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        if(mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Find which spinner value was selected
        // Set distance accordingly
        if(position == 0) {
            distance = null;
        }
        else {

            distance = parent.getItemAtPosition(position).toString();
        }

        /* If both direction and distance have been chosen, execute the listLStops
            method to list the L stops that are within the selected distance with trains
            that are going in the selected direction
        */
        if (direction != null && distance !=null) {
            if(!mRequestingLocationUpdates) {
                mRequestingLocationUpdates = true;
                startLocationUpdates();
            }
            listLStops();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        distance = null;
    }

    /**
     * Call LStopListActivity to display the list of L stops
     */
    private void listLStops() {

        // Toast.makeText(this,"Direction: " + direction + "\nDistance: " + distance,Toast.LENGTH_LONG).show();
        LStopSearchTerms lStopSearchTerms = new LStopSearchTerms();

        lStopSearchTerms.setCity("Chicago");
        lStopSearchTerms.setDirection(direction);
        lStopSearchTerms.setDistance(distance);

        mRequestingLocationUpdates = false;
        stopLocationUpdates();

        if(mLastLocation != null) {

            String myLatitude = Double.toString(mLastLocation.getLatitude());
            String myLongitude = Double.toString(mLastLocation.getLongitude());

            Log.i(TAG, "My latitude = " + myLatitude + " My longitude = " + myLongitude);

            lStopSearchTerms.setLatitude(myLatitude);
            lStopSearchTerms.setLongitude(myLongitude);

            Intent listLStopsIntent = new Intent(this,LStopListActivity.class);
            listLStopsIntent.putExtra(LStopSearchTerms.LSTOPSEARCHTERMS, lStopSearchTerms);
            //listLStopsIntent.putExtra(LStopListFragment.LSTOPDATABASE, lStopDB);
            startActivity(listLStopsIntent);

        }
        else {

            Toast.makeText(this,"Couldn't get the location. Make sure location is enabled on the device",Toast.LENGTH_LONG).show();
        }
    }

    private void getLocation() {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATGE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(resultCode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"This device is not supported.",Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getLocation();
        if(mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        getLocation();
    }
}
