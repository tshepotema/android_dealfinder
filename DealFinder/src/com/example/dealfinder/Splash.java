package com.example.dealfinder;

import android.app.Activity;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Thread appStatus = new Thread() {
			public void run() {
				try {
					//Call the stores service
					StoresService stores = new StoresService(Splash.this);
					stores.getUpdatedStores();
					//Call the featured deals service
					FeaturedDealsService featuredDeals = new FeaturedDealsService(Splash.this);
					featuredDeals.getFeaturedDeals();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		appStatus.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();	//kill the splash activity
	}		
}
