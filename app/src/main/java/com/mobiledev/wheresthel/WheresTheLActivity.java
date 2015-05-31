package com.mobiledev.wheresthel;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Terry on 5/31/2015.
 */
public abstract class WheresTheLActivity extends ActionBarActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        int currentMenuId = getCurrentMenuId();
        if(currentMenuId != 0){
            menu.removeItem(currentMenuId);
        }

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

    /**
     * This method is invoked when the user clicks the Where's The L? Menu option.
     * @param menuItem
     */
    public void wheresTheLClicked(MenuItem menuItem) {

        Intent wheresTheLIntent = new Intent(this, MainActivity.class);
        wheresTheLIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(wheresTheLIntent);
        finish();
    }

    public abstract int getCurrentMenuId();
}
