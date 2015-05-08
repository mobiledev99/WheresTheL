package com.mobiledev.wheresthel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * MainActivity displays the main selection page used to find a list of L stops based on
 * travel direction and a selected distance from the user
 * * @author Terry Chaisson
 * @version %I%, %G%
 *
 */
public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private RadioGroup rgDirectionGroup;
    private RadioButton rbNorth, rbSouth, rbEast, rbWest;
    private Spinner spnDistance;

    public String direction;
    public String distance;

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        Intent listLStopsIntent = new Intent(this,LStopListActivity.class);
        startActivity(listLStopsIntent);
    }
}
