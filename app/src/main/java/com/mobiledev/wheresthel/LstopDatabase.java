package com.mobiledev.wheresthel;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terry on 5/10/2015.
 */
public class LstopDatabase extends SQLiteAssetHelper implements Serializable, FetchLStops {

    public static final String LSTOPDATABASE = "LSTOPDATABASE";

    //private static final String DATABASE_NAME = "wheresthel.db";
    private static final String DATABASE_NAME = "LStops.db";
    private static final int DATABASE_VERSION = 1;

    // LStop table
    public static final String LSTOP = "LSTOP";
    public static final String DIRECTION_ID = "DIRECTION_ID";
    public static final String STOP_NAME = "STOP_NAME";
    public static final String STATION_NAME = "STATION_NAME";
    public static final String STATION_DESCRIPTIVE_NAME = "STATION_DESCRIPTIVE_NAME";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String SIN_LATITUDE_RAD = "SIN_LAT_RAD";
    public static final String SIN_LONGITUDE_RAD = "SIN_LON_RAD";
    public static final String COS_LATITUDE_RAD = "COS_LAT_RAD";
    public static final String COS_LONGITUDE_RAD = "COS_LON_RAD";



    public LstopDatabase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public ArrayList<LStop> fetchLStopsWithinDistance(LStopQueryValues queryValues) throws IOException {

        String sql = "SELECT * FROM " + LSTOP;
        String where = " WHERE (CAST(" + LATITUDE + " AS DOUBLE) >= " + queryValues.getMinLat() +
                " AND (CAST(" + LATITUDE + " AS DOUBLE) <= " + queryValues.getMaxLat() + "))" +
                " AND (CAST(" + LONGITUDE + " AS DOUBLE) >= " + queryValues.getMinLon() +
                " " + queryValues.getOrAnd() + " (CAST(" + LONGITUDE + " AS DOUBLE) <= " +
                queryValues.getMaxLon() + "))";
        String queryCircle = " AND (" + queryValues.getSinLat() + " * CAST(" +
                SIN_LATITUDE_RAD + " AS DOUBLE) + " + queryValues.getCosLat() + " * " +
                        "CAST(" +COS_LATITUDE_RAD + " AS DOUBLE) * (CAST(" + COS_LONGITUDE_RAD +
                " AS DOUBLE) * " + queryValues.getCosLon() + " + CAST(" + SIN_LONGITUDE_RAD +
                " AS DOUBLE) * " + queryValues.getSinLon() + ")) >= " +
                queryValues.getAngularRadius();

        String direction = " AND " + DIRECTION_ID + " = '"  + queryValues.direction + "'";

//        String queryCircle = " AND acos(sin(" + queryValues.getSinLat() + ") * sin(CAST(" +
//                LATITUDE + " AS DOUBLE)) + cos(" + queryValues.getCosLat() + ") * cos(CAST(" +
//                LATITUDE + " AS DOUBLE)) * cos(CAST(" +
//                LONGITUDE + " AS DOUBLE) - " + queryValues.getCosLon() + ")) <= " +
//                queryValues.getAngularRadius();


        return (innerSelect(sql + where + queryCircle + direction));
    }

    private ArrayList<LStop> innerSelect(String sql) {

        Log.i(LSTOPDATABASE, "LStopQueryString: " + sql);

                ArrayList < LStop > matchingLStops = new ArrayList<LStop>();

        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                LStop lStop = new LStop();

                lStop.setDirection_id(cursor.getString(cursor.getColumnIndex(DIRECTION_ID)));
                lStop.setStop_name(cursor.getString(cursor.getColumnIndex(STOP_NAME)));
                lStop.setStation_name(cursor.getString(cursor.getColumnIndex(STATION_NAME)));
                lStop.setStation_descriptive_name(cursor.getString(cursor.getColumnIndex(STATION_DESCRIPTIVE_NAME)));
                lStop.setLatitude(cursor.getString(cursor.getColumnIndex(LATITUDE)));
                lStop.setLongitude(cursor.getString(cursor.getColumnIndex(LONGITUDE)));

                matchingLStops.add(lStop);
                cursor.moveToNext();

            }
        }

        cursor.close();
        return matchingLStops;
    }

}
