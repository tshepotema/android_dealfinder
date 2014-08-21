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
import com.example.dealfinder.database.StoresTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class StoresService {
	
	private Context storesServiceContext;

	public StoresService(Context storesServiceContext) {
		this.storesServiceContext = storesServiceContext;
	}
	
	public void getUpdatedStores() {
        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask(this.storesServiceContext).execute("http://dealfinder.zetail.co.za/webservices/deals.php");
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
            
            jsonRequest.put("action", "getUpdatedStores");
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
                result = "Failed to retrieve updated stores";

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
            Log.d("postExecuteStoresService", "starting to PostExecute StoresService");
            try {
            	
            	//OPEN THE DATABASE
            	DealsDatabaseHelper dbHelper = new DealsDatabaseHelper(this.syncContext);
            	SQLiteDatabase db = dbHelper.getWritableDatabase();
            	
            	ContentValues values = new ContentValues();
            	            	
                JSONObject resultObj = new JSONObject(result);
                JSONArray response = resultObj.getJSONArray("response");
                Log.d("postExecuteStoresService", "trying to PostExecute on StoresService [" + response.length() + "] items");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject dealObj = response.getJSONObject(i);

                    Integer storeID = dealObj.getInt("store_id");
                    String store = dealObj.getString("store");
                    String telephone = dealObj.getString("telephone");
                    String fax = dealObj.getString("fax");
                    String email = dealObj.getString("email");
                    String tradingHrs = dealObj.getString("trading_hrs");
                    String address = dealObj.getString("address");
                    String gpsLat = dealObj.getString("gps_lat");
                    String gpsLon = dealObj.getString("gps_lon");

                    //create content values
                    values.put(StoresTable.COLUMN_ID, storeID);
                    values.put(StoresTable.COLUMN_STORE, store);
                    values.put(StoresTable.COLUMN_TEL, telephone);
                    values.put(StoresTable.COLUMN_FAX, fax);
                    values.put(StoresTable.COLUMN_EMAIL, email);
                    values.put(StoresTable.COLUMN_TRD_HRS, tradingHrs);
                    values.put(StoresTable.COLUMN_ADDRESS, address);
                    values.put(StoresTable.COLUMN_GPS_LAT, gpsLat);
                    values.put(StoresTable.COLUMN_GPS_LON, gpsLon);
                    
                    //insert the data into the database using a prepared statement
                    db.insertWithOnConflict(StoresTable.TABLE_STORES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    
                    Log.d("postExecuteStoresServive", " --> store =  [" + store + "] items");                                        
                }
                
                //CLOSE THE DATABASE
                db.close();
                dbHelper.close();
                
            } catch (JSONException e) {
                e.printStackTrace();
            }                                    
        }
    }		
}