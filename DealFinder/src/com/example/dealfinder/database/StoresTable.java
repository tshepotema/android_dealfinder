package com.example.dealfinder.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StoresTable {
	//Database Table
	public static final String DATABASE_DEALS = "deals.db";
	public static final String TABLE_STORES = "stores";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_STORE = "store";
	public static final String COLUMN_TEL = "tel";
	public static final String COLUMN_FAX = "fax";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_TRD_HRS = "trading_hrs";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_GPS_LAT = "gps_lat";
	public static final String COLUMN_GPS_LON = "gps_lon";
	
	//Logging tag
	private static final String TAG = StoresTable.class.getSimpleName();
	
	//Database Creation SQL
	public static final String DATABASE_CREATE = "" +
			"CREATE TABLE " + TABLE_STORES + " (" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_STORE + " TEXT NOT NULL, " + 
			COLUMN_TEL + " TEXT NOT NULL, " + 
			COLUMN_FAX + " TEXT NOT NULL, " + 
			COLUMN_EMAIL + " TEXT NOT NULL, " + 
			COLUMN_TRD_HRS + " TEXT NOT NULL, " + 
			COLUMN_ADDRESS + " TEXT NOT NULL, " + 
			COLUMN_GPS_LAT + " TEXT NOT NULL, " + 
			COLUMN_GPS_LON + " TEXT NOT NULL);";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		Log.d(TAG, "stores table has been created");
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		Log.d(TAG, "Upgrading database from ver " + oldVer + " to ver " + newVer);
		database.execSQL("DROP IF EXISTS TABLE " + TABLE_STORES);
		onCreate(database);
	}
}