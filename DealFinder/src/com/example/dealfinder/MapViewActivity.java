package com.example.dealfinder;

import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.dealfinder.database.StoresTable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends Activity implements LocationListener {

	private GoogleMap googlemap;
	private int storeIcon, userIcon;
	private Marker userMarker, storeMarker;

	@Override
	protected void onCreate(Bundle savedIntanceState) {
		super.onCreate(savedIntanceState);
		if (isGooglePlay()) {
			setContentView(R.layout.map_view);
			Log.d("MapsAct", "before map setup");
			
			setUpMap();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Intent openSearch = new Intent("com.example.dealfinder.SEARCH");
			startActivity(openSearch);
			return true;
		case R.id.action_stores_list:
			//show the stores list
			Intent openStoresList = new Intent("com.example.dealfinder.STORESLIST");
			startActivity(openStoresList);
			return true;
		case R.id.action_featured_deals:
			//show the map with stores location
			Intent openFeaturedDeals = new Intent("com.example.dealfinder.FEATUREDDEALS");
			startActivity(openFeaturedDeals);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());

		googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 13));	//zoom 1 - 20
		
		storeIcon = R.drawable.ic_action_location_found;
		userIcon = R.drawable.ic_action_person;
		
		if (userMarker != null) {
			userMarker.remove();
		}
		
		userMarker = googlemap.addMarker(
				new MarkerOptions()
			.position(lastLatLng)
			.title("You are here")
			.icon(BitmapDescriptorFactory.fromResource(userIcon))
			.snippet("Current Location"));
		Log.d("MapsAct", "--maps-- done setting markers --" + lastLatLng.toString());		
		
		/*
		String storeName, address, lat, lon;
		// start-- mark the stores
		SQLiteDatabase db;
		db = openOrCreateDatabase(StoresTable.DATABASE_DEALS,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());

		Cursor cur = db.query(StoresTable.TABLE_STORES, null, null, null, null,
				null, null);
		cur.moveToFirst();
		while (cur.isAfterLast() == false) {
			storeName = cur.getString(1); 	// store
			address = cur.getString(6); 	// address
			lat = cur.getString(7); // latitude
			lon = cur.getString(8); // longitude
			
			Double latDbl = Double.parseDouble(lat);
			Double lonDbl = Double.parseDouble(lon);
			
			Log.d("MapsAct", "--maps-- store dbl lat = " + latDbl + " :: store dbl lon = " + lonDbl);

			LatLng storeLatLng = new LatLng(latDbl, lonDbl);

			storeMarker = googlemap.addMarker(new MarkerOptions()
					.position(storeLatLng)
					.title(storeName)
					.icon(BitmapDescriptorFactory
					.fromResource(this.storeIcon))
					.snippet("" + address));
			cur.moveToNext();
		}
		// clean up and close the database and resources
		cur.close();
		db.close();
		// end-- mark the stores
		*/
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	private void setUpMap() {
		if (googlemap == null) {

			googlemap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			if (googlemap != null) {

				// initialize map
				googlemap.setMyLocationEnabled(true);

				LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

				String provider = lm.getBestProvider(new Criteria(), true);

				if (provider == null) {
					onProviderDisabled(provider);
				}

				Location lastLoc = lm.getLastKnownLocation(provider);
				if (lastLoc != null) {					
					onLocationChanged(lastLoc);
				} else {
					lastLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (lastLoc != null) {
						onLocationChanged(lastLoc);
					}
				}
			}
		}
	}

	private boolean isGooglePlay() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (status == ConnectionResult.SUCCESS) {
			return true;
		} else {
			// Google Play is not available
			((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 10)).show();
		}
		return false;
	}

}