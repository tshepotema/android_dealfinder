package com.example.dealfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendOrder extends Activity {
	
	EditText etNames, etContactNumber, etMessage;
	Button btSend, btCancel;
	
	String[] emailAddress = {"tshepotema@gmail.com", "tshepo927@gmail.com"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_order);
		
		initializeView();
		
		btSend.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String message = etMessage.getText().toString();
				
				Intent sendEmailIntent = new Intent(android.content.Intent.ACTION_SEND);
				sendEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailAddress);
				sendEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Deal - ");
				sendEmailIntent.setType("plain/text");
				sendEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
				startActivity(sendEmailIntent);
			}
		});
		
		btCancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	public void initializeView() {
		etNames = (EditText) findViewById(R.id.etNames);
		etContactNumber = (EditText) findViewById(R.id.etContactNumber);
		etMessage = (EditText) findViewById(R.id.etMessage);
		btSend = (Button) findViewById(R.id.btSend);
		btCancel = (Button) findViewById(R.id.btCancel);
	}

	@Override
	public void onPause() {
		super.onPause();
		finish();
	}

}
