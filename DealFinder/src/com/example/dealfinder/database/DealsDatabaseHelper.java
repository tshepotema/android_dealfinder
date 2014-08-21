package com.example.dealfinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DealsDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "deals.db";
	private static final int DATABASE_VERSION = 1;

	public DealsDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase db) {
		DealsTable.onCreate(db);
		StoresTable.onCreate(db);
	}

	//called during the upgrade of the database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DealsTable.onUpgrade(db, oldVersion, newVersion);
		StoresTable.onUpgrade(db, oldVersion, newVersion);
	}

}