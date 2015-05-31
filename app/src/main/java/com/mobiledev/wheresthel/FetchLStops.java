package com.mobiledev.wheresthel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Terry on 5/31/2015.
 */
public interface FetchLStops {

    /**
     * Fetch Lstops that are within a given distance to the user.
     * @param queryValues
     * @return
     * @throws IOException
     */
    public ArrayList<LStop> fetchLStopsWithinDistance(LStopQueryValues queryValues) throws IOException;
}
