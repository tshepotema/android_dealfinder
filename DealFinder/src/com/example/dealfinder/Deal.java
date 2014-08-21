package com.example.dealfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Deal extends Activity {

	TextView tvDealTitle, tvPrice, tvNetwork, tvDescription;
	ImageView ivDealImage, ivNetwork;
	ImageButton ibOrderNow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deal_detail);
		
		initializeLayout();
		
		ibOrderNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(Deal.this, "Getting Deal - " + tvDealTitle.getText(), Toast.LENGTH_LONG).show();
				Intent sendOrder = new Intent("com.example.dealfinder.SENDORDER");
				startActivity(sendOrder);
			}
		}); 
		
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
		case R.id.action_store_locator:
			//show the map with stores location
			Intent openStoreLocator = new Intent("com.example.dealfinder.MAPVIEWACTIVITY");
			startActivity(openStoreLocator);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	private void initializeLayout() {				
		tvDealTitle = (TextView) findViewById(R.id.tvDealTitle);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvNetwork = (TextView) findViewById(R.id.tvNetwork);
		tvDescription = (TextView) findViewById(R.id.tvDescription);		
		
		ivDealImage = (ImageView) findViewById(R.id.ivDealImage);
		ivNetwork = (ImageView) findViewById(R.id.ivNetwork);
		
		ibOrderNow = (ImageButton) findViewById(R.id.ibOrderNow);
		
		Bundle dealDetails = getIntent().getExtras();		
		String dealTitle = dealDetails.getString("deal_title");
		
		//String dealID = dealDetails.getString("deal_id");
		String dealImage = dealDetails.getString("deal_image");		
		Picasso.with(Deal.this).load(dealImage).placeholder(R.drawable.placeholder).into(ivDealImage);
	
		String dealDescription = dealDetails.getString("deal_description");
		String dealNetwork = dealDetails.getString("deal_network");
		String dealPrice = dealDetails.getString("deal_price");
			
		tvDealTitle.setText(dealTitle);
		tvPrice.setText("R " + dealPrice);
		tvDescription.setText(dealDescription);
		
		int networkDrawable = R.drawable.voda;
		
		String networkOperator = "";		
		if (dealNetwork == "1") {
			networkOperator = "On Vodacom";
			networkDrawable = R.drawable.voda;
		} else if (dealNetwork == "2") {
			networkOperator = "On MTN";
			networkDrawable = R.drawable.mtn;
		} else if (dealNetwork == "3") {
			networkOperator = "On CELLC";
			networkDrawable = R.drawable.cellc;
		}
		
		tvNetwork.setText(networkOperator);
		ivNetwork.setImageResource(networkDrawable);
	}	
}