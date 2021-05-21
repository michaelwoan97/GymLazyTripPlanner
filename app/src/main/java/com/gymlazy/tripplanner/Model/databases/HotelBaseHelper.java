package com.gymlazy.tripplanner.Model.databases;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HotelBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DB_NAME = "trip_hotels";
    private static InputStream sHotelSQL;
    private static final String TAG = "HotelBaseHelper";

    public HotelBaseHelper(Context packageContext) throws IOException {
        super(packageContext, DB_NAME, null, VERSION);

        // get the path of asset folder
        sHotelSQL = packageContext.getResources().getAssets().open("trip_hotels.sql");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        int col = 0;
        // if database does not exist look for sql file to generate database for hotel data
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(sHotelSQL));

        // execute insert sql statement for hotel data
        while (true) {
            try {
                if (!insertReader.ready()) break;
                String sqlStatement = insertReader.readLine();
                db.execSQL(sqlStatement);
                Log.d(TAG, "Executed column " + col + " " + sqlStatement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // close buffer reader
        try {
            insertReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    
}
