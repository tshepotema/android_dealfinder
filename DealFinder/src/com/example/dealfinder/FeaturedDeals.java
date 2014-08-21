package com.example.dealfinder;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dealfinder.database.DealsTable;

//import android.util.Log;

public class FeaturedDeals extends Activity {
	ListView list;

	ArrayList<String> dealTitle = new ArrayList<String>();
	ArrayList<String> dealPrice = new ArrayList<String>();
	ArrayList<String> imageId = new ArrayList<String>();

	ArrayList<String> description = new ArrayList<String>();
	ArrayList<String> network = new ArrayList<String>();
	ArrayList<String> dealID = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_featured_deals);

		readFeaturedDeals(); // get the featured deals from db

		CustomList adapter = new CustomList(FeaturedDeals.this, dealTitle,
				dealPrice, imageId, description, network, dealID);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle dealDetails = new Bundle();
				dealDetails.putString("deal_id", dealID.get(position));
				dealDetails.putString("deal_title", dealTitle.get(position));
				dealDetails.putString("deal_price", dealPrice.get(position));
				dealDetails.putString("deal_description", description.get(position));
				dealDetails.putString("deal_network", network.get(position));
				dealDetails.putString("deal_image", imageId.get(position));
				Intent viewDetail = new Intent(FeaturedDeals.this, Deal.class);
				viewDetail.putExtras(dealDetails);
				startActivity(viewDetail);
			}
		});
	}

	public void readFeaturedDeals() {
		SQLiteDatabase db;
		db = openOrCreateDatabase(DealsTable.DATABASE_DEALS,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());

		Cursor cur = db.query(DealsTable.TABLE_DEALS, null, null, null, null,
				null, null);
		cur.moveToFirst();

		while (cur.isAfterLast() == false) {
			dealTitle.add(cur.getString(1)); 	// deal
			dealPrice.add(cur.getString(2)); 	// price
			imageId.add(cur.getString(4)); 		// image

			dealID.add(cur.getString(0)); 		// deal_id
			network.add(cur.getString(3)); 		// network
			description.add(cur.getString(5)); 	// description

			cur.moveToNext();
		}
		cur.close();
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
		case R.id.action_store_locator:
			//show the map with stores location
			Intent openStoreLocator = new Intent("com.example.dealfinder.MAPVIEWACTIVITY");
			startActivity(openStoreLocator);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}