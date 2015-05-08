package com.mobiledev.wheresthel;

import android.app.Fragment;

/**
 * Created by Terry on 5/2/2015.
 *
 * Activity returns a new instance of an LStopListFragment
 * used to display a list of L stops
 *
 * @author Terry Chaisson
 * @version %I%, %G%
 *
 */
public class LStopListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LStopListFragment();
    }
}
