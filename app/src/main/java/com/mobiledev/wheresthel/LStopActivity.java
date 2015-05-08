package com.mobiledev.wheresthel;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

    /**
     * Gets an LStop object from the calling intent and returns a fragment
     * containing the LStop object for display
     *
     * @return a new fragment with the Lstop object to be displayed
     */

    @Override
    protected Fragment createFragment() {
        LStop lstop = (LStop) getIntent().getSerializableExtra(LStopFragment.LSTOP);

        return LStopFragment.newInstance(lstop);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lstop, menu);
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
}
