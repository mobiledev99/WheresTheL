package com.mobiledev.wheresthel;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Terry on 5/2/2015.
 *
 * Creates instances of LStop objects for testing
 *
 * @author Terry Chaisson
 * @version %I%, %G%
 *
 */
public class LStopFactory implements FetchLStops{
    private ArrayList<LStop> mLStops; // ArrayList of LStop objects

    private static LStopFactory sLStopFactory; // Singleton LStopFactory object reference
    private Context mAppContext; // Application Context reference

    private LStopFactory(Context appContext) {
        mAppContext = appContext;
        mLStops = new ArrayList<LStop>();
        String[] names = {"Garfield", "Lake", "Harrison", "Michigan", "Clinton",
                            "Erie", "Jefferson", "Superior", "Jackson", "Huron",
                            "Stevenson", "Ontario", "Chicago", "Eisenhower","Des Planes",
                            "Lincoln", "Fox", "Washington", "Illinois", "Cleveland"};
        for(int i = 0; i < 20; i++) {
            LStop l = new LStop();
            l.setDirection_id("N");
            l.setStop_name(i + 1 + "th");
            l.setStation_name(i + 1 + "th " + names[i]);
            l.setStation_descriptive_name(i + 1 + "th " + names[i] + " Station");
            l.setLatitude(i + 41.795172 + "");
            l.setLongitude(i - 87.618327 + "");
            mLStops.add(l);
        }
    }

    /**
     *
     * Returns a singleton LStopFactory object
     *
     * If the object already exists, return it
     * else, create an instance an return it
     * @param c a context
     * @return an instance of LstopFactory
     */
    public static LStopFactory get(Context c) {
        if(sLStopFactory == null) {
            sLStopFactory = new LStopFactory(c.getApplicationContext());
        }
        return sLStopFactory;
    }

    /**
     * Returns the ArrrayList of LStops
     * @return an ArrayList of LStop objects
     */
    public ArrayList<LStop> fetchLStopsWithinDistance(LStopQueryValues queryValues) {
        return mLStops;
    }

    /**
     * Returns an LStop objectt from the ArrayList of LStop objects
     *
     * Utility method not used yet
     *
     * @param stopName the name of the L stop to be returned
     * @return an LStop object that matches the stopName passed in
     */
    public LStop getLStop(String stopName) {
        for (LStop l : mLStops) {
            if(l.getStation_name().equals(stopName))
                return l;
        }
        return null;
    }
}
