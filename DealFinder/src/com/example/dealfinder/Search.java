package com.example.dealfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Search extends Activity implements OnItemSelectedListener {
	ListView lvSearch;

	final ArrayList<String> dealTitle = new ArrayList<String>();
	final ArrayList<String> dealPrice = new ArrayList<String>();
	final ArrayList<String> imageId = new ArrayList<String>();

	final ArrayList<String> description = new ArrayList<String>();
	final ArrayList<String> network = new ArrayList<String>();
	final ArrayList<String> dealID = new ArrayList<String>();
	
	Spinner spPrices;
	
	private static String selectedPrice = "Any";
	
	public void getAllDeals() {
        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://dealfinder.zetail.co.za/webservices/deals.php");
	}
		
	public void getDealsByPrice() {
        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute("http://dealfinder.zetail.co.za/webservices/deals.php");
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);		
		
		getDealsByPrice(); //getAllDeals(); // get the initial deals from web service
		
		 spPrices = (Spinner) findViewById(R.id.spPrices);
		// Create an ArrayAdapter using the string array and a default spinner layout
		 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		         R.array.prices_array, android.R.layout.simple_spinner_item);
		 // Specify the layout to use when the list of choices appears
		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spPrices.setAdapter(adapter);
		 spPrices.setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		dealTitle.clear();
		dealPrice.clear();
		imageId.clear();
		description.clear();
		network.clear();
		dealID.clear();
		CustomList adapter = new CustomList(Search.this, dealTitle,
				dealPrice, imageId, description, network, dealID);
		lvSearch = (ListView) findViewById(R.id.lvSearch);
		lvSearch.setAdapter(adapter);
		adapter.clear();					//clear old data
		adapter.notifyDataSetChanged();		//clear old data
		
		setSelectedPrice(spPrices.getSelectedItem().toString());
		getDealsByPrice();
		Toast.makeText(Search.this, "Retrieving deals in price range " + getSelectedPrice(), Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }	
		
    public static String GET(String url){
        InputStream inputStream;
        String result = "";
        String price = Search.getSelectedPrice();
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            
            HttpPost post = new HttpPost(url);

            JSONObject jsonRequest = new JSONObject();
            JSONObject requestData = new JSONObject();
            requestData.put("price", price);
            JSONArray requestDataArray = new JSONArray();
            requestDataArray.put(requestData);
            
            //jsonRequest.put("action", "getAllDeals");
            jsonRequest.put("action", "getDealsByPrice");
            jsonRequest.put("data", requestDataArray);            	
            StringEntity reqEntity = new StringEntity(jsonRequest.toString());
            reqEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(reqEntity);
            
            // make POST request to the url
            HttpResponse httpResponse = httpclient.execute(post);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static String getSelectedPrice() {
		return selectedPrice;
	}

	public static void setSelectedPrice(String selectedPrice) {
		Search.selectedPrice = selectedPrice;
	}

	public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("postExecuteSearch", "starting to PostExecute");
            try {
            	    
                JSONObject resultObj = new JSONObject(result);
                JSONArray response = resultObj.getJSONArray("response");
                Log.d("postExecuteSearch", "trying PostExecute on [" + response.length() + "] items");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject dealObj = response.getJSONObject(i);

                    Integer deal_id = dealObj.getInt("deal_id");
                    String deal = dealObj.getString("deal");
                    String price = dealObj.getString("price");
                    ////String created = dealObj.getString("created");
                    String dealDescription = dealObj.getString("deal_description");
                    String dealImg = dealObj.getString("deal_image");
                    String networkID = dealObj.getString("network");
                    ////String vapID = dealObj.getString("vap_id");

        			dealTitle.add(deal); 	// deal
        			dealPrice.add(price); 	// price
        			imageId.add(dealImg); 		// image

        			dealID.add(deal_id.toString()); 		// deal_id
        			network.add(networkID); 		// network
        			description.add(dealDescription); 	// description
                                                            
                    Log.d("postExecute", " --> img =  [" + dealImg + "] <- searchService");                                        
                }
                                
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            //once the service is done collecting data

    		CustomList adapter = new CustomList(Search.this, dealTitle,
    				dealPrice, imageId, description, network, dealID);
    		lvSearch = (ListView) findViewById(R.id.lvSearch);
    		lvSearch.setAdapter(adapter);
    		//adapter.clear();					//clear old data
    		adapter.notifyDataSetChanged();		//clear old data
    		lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    				Intent viewDetail = new Intent(Search.this, Deal.class);
    				viewDetail.putExtras(dealDetails);
    				startActivity(viewDetail);
    			}
    		});    		            
                        
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