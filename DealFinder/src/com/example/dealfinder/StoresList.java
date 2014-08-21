package com.example.dealfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.example.dealfinder.database.StoresTable;

public class StoresList extends Activity {
	ExpandableListAdapterCustom listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	//HashMap<String, List<String>> listDataChild;
	HashMap<String, List<String>> listDataChild;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stores_main);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapterCustom(this, listDataHeader, listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				/*Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Expanded",
						Toast.LENGTH_SHORT).show();*/
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				/*Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Collapsed",
						Toast.LENGTH_SHORT).show();*/

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				/*Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ listDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();*/
				
				Intent showStoreMap = new Intent("com.example.dealfinder.STOREMAP");
				startActivity(showStoreMap);
				
				return false;
			}
		});
				
	}
	
	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		
		String storeID, store, tel, fax, email, tradeHrs, address, distance;
		
		SQLiteDatabase db;
		db = openOrCreateDatabase(StoresTable.DATABASE_DEALS, SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());

		Cursor cur = db.query(StoresTable.TABLE_STORES, null, null, null, null, null, null);
		cur.moveToFirst();
		int iCount = 0;
		while (cur.isAfterLast() == false) {
			storeID = cur.getString(0);			// store id
			store = cur.getString(1); 			// store
			tel = cur.getString(2); 			// tel
			fax = cur.getString(3); 			// fax
			email = cur.getString(4); 			// email
			tradeHrs = cur.getString(5); 		// tradeHrs
			address = cur.getString(6); 		// address			
			//TODO: get the store's gps						
			//get the current location and then calculate the distance between the two points
			distance = "** KM away";		//TODO: calculate distance
			
			// Adding headers data
			listDataHeader.add(store);
			
			// Adding body data
			List<String> storeData = new ArrayList<String>();
			storeData.add(tel);
			storeData.add(fax);
			storeData.add(email);
			storeData.add(tradeHrs);
			storeData.add(address);
			storeData.add(distance);
			storeData.add(storeID);
			
			listDataChild.put(listDataHeader.get(iCount), storeData); // Header, Child data
			
			cur.moveToNext();
			iCount++;
		}
        //close the database and resources
		cur.close();	
        db.close();		
	}
	
}