package com.mobiledev.wheresthel;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

/**
 * Created by Terry on 5/2/2015.
 *
 * SingleFragmentActivity is an abstract class that is used generic
 * activity for hosting fragments
 *
 * It required the implementation of abstract method createFragment
 *
 * @author Terry Chaisson
 * @version %I%, %G%
 *
 */
public abstract class SingleFragmentActivity extends Activity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();

            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

    }
}