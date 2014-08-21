package com.example.dealfinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

import com.example.dealfinder.database.DealsDatabaseHelper;
import com.example.dealfinder.database.DealsTable;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class FeaturedDealsService {
	
	private Context featuredServiceContext;

	public FeaturedDealsService(Context featuredServiceContext) {
		this.featuredServiceContext = featuredServiceContext;
	}
	
	public void getFeaturedDeals() {
        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask(this.featuredServiceContext).execute("http://dealfinder.zetail.co.za/webservices/deals.php");
	}
	
    public static String GET(String url){
        InputStream inputStream;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            
            HttpPost post = new HttpPost(url);

            JSONObject jsonRequest = new JSONObject();
            JSONObject requestData = new JSONObject();
            requestData.put("db_ver", "1");
            JSONArray requestDataArray = new JSONArray();
            requestDataArray.put(requestData);
            
            jsonRequest.put("action", "getFeaturedDeals");
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

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private Context syncContext;
        
        public HttpAsyncTask(Context context) {
        	syncContext = context;
        }

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Log.d("postExecute", "starting to PostExecute");
            try {
            	
            	//OPEN THE DATABASE
            	DealsDatabaseHelper dbHelper = new DealsDatabaseHelper(this.syncContext);
            	SQLiteDatabase db = dbHelper.getWritableDatabase();
            	
            	ContentValues values = new ContentValues();
            	            	
                JSONObject resultObj = new JSONObject(result);
                JSONArray response = resultObj.getJSONArray("response");
                //Log.d("postExecute", "trying PostExecute on [" + response.length() + "] items");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject dealObj = response.getJSONObject(i);

                    Integer dealID = dealObj.getInt("deal_id");
                    String deal = dealObj.getString("deal");
                    String price = dealObj.getString("price");
                    //String created = dealObj.getString("created");
                    String dealDescription = dealObj.getString("deal_description");
                    String dealImg = dealObj.getString("deal_image");
                    String network = dealObj.getString("network");
                    //String vapID = dealObj.getString("vap_id");

                    //create content values
                    values.put(DealsTable.COLUMN_ID, dealID);
                    values.put(DealsTable.COLUMN_DEAL, deal);
                    values.put(DealsTable.COLUMN_PRICE, price);
                    values.put(DealsTable.COLUMN_DESCRIPTION, dealDescription);
                    values.put(DealsTable.COLUMN_IMAGE, dealImg);
                    values.put(DealsTable.COLUMN_NETWORK, network);
                    
                    //insert the data into the database using a prepared statement
                    db.insertWithOnConflict(DealsTable.TABLE_DEALS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    
                    //Log.d("postExecute", " --> img =  [" + dealImg + "] items");                                        
                }
                
                //CLOSE THE DATABASE
                db.close();
                dbHelper.close();
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
			Intent mainActIntent = new Intent("com.example.dealfinder.FEATUREDDEALS");
			//Intent mainActIntent = new Intent("com.example.dealfinder.MAINACTIVITY");					
			syncContext.startActivity(mainActIntent);					
                        
        }
    }		
}