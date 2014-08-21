package com.example.dealfinder.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import com.example.dealfinder.database.DealsDatabaseHelper;
import com.example.dealfinder.database.StoresTable;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyStoresContentProvider extends ContentProvider {

	//the database to be used
	private DealsDatabaseHelper database;
	
	//used for the URI match
	private static final int STORES = 10;
	private static final int STORE_ID = 20;
	private static final String AUTHORITY = "com.example.dealfinder.contentprovider";
	private static final String BASE_PATH = "stores";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/stores";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/store";
	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, STORES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", STORE_ID);
	}

	@Override
	public boolean onCreate() {
		database = new DealsDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// Check if the caller has requested a column which does not exists
		checkColumns(projection);
		// Set the table
		queryBuilder.setTables(StoresTable.TABLE_STORES);
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case STORES:
			break;
		case STORE_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(StoresTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case STORES:
			id = sqlDB.insert(StoresTable.TABLE_STORES, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case STORES:
			rowsDeleted = sqlDB.delete(StoresTable.TABLE_STORES, selection,
					selectionArgs);
			break;
		case STORE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(StoresTable.TABLE_STORES,
						StoresTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(StoresTable.TABLE_STORES,
						StoresTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case STORES:
			rowsUpdated = sqlDB.update(StoresTable.TABLE_STORES, values, selection,
					selectionArgs);
			break;
		case STORE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(StoresTable.TABLE_STORES, values,
						StoresTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(StoresTable.TABLE_STORES, values,
						StoresTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { StoresTable.COLUMN_STORE, StoresTable.COLUMN_TEL,
				StoresTable.COLUMN_FAX, StoresTable.COLUMN_EMAIL, StoresTable.COLUMN_TRD_HRS,
				StoresTable.COLUMN_ADDRESS, StoresTable.COLUMN_GPS_LAT, StoresTable.COLUMN_GPS_LON,
				StoresTable.COLUMN_ID };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}
}
