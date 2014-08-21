package com.example.dealfinder.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DealsTable {
	//Database Table
	public static final String DATABASE_DEALS = "deals.db";
	public static final String TABLE_DEALS = "deals";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DEAL = "deal";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_NETWORK = "network";
	public static final String COLUMN_IMAGE = "image";
	public static final String COLUMN_DESCRIPTION = "description";
	
	//Logging tag
	private static final String TAG = DealsTable.class.getSimpleName();
	
	//Database Creation SQL
	public static final String DATABASE_CREATE = "" +
			"CREATE TABLE " + TABLE_DEALS + " (" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_DEAL + " TEXT NOT NULL, " + 
			COLUMN_PRICE + " TEXT NOT NULL, " + 
			COLUMN_NETWORK + " TEXT NOT NULL, " + 
			COLUMN_IMAGE + " TEXT NOT NULL, " + 
			COLUMN_DESCRIPTION + " TEXT NOT NULL);";
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		Log.d(TAG, "deals table has been created");
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVer, int newVer) {
		Log.d(TAG, "Upgrading database from ver " + oldVer + " to ver " + newVer);
		database.execSQL("DROP IF EXISTS TABLE " + TABLE_DEALS);
		onCreate(database);
	}
}
