package com.example.dealfinder;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {
	
	private final Activity context;
	
	private final ArrayList<String> dealTitle;
	private final ArrayList<String> dealPrice;
	private final ArrayList<String> imageId;
	
	private final ArrayList<String> description;
	private final ArrayList<String> network;
	private final ArrayList<String> dealID;
	
	public CustomList(Activity context, ArrayList<String> dealTitle2, 
		ArrayList<String> dealPrice2, ArrayList<String> imageId2, 
		ArrayList<String> description2, ArrayList<String> network2, ArrayList<String> dealID2) {
		super(context, R.layout.list_single, dealTitle2);
		this.context = context;
		this.dealTitle = dealTitle2;
		this.dealPrice = dealPrice2;
		this.imageId = imageId2;
		this.description = description2;
		this.network = network2;
		this.dealID = dealID2;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		
		View rowView = inflater.inflate(R.layout.list_single, null, true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.deal_title);
		TextView txtPrice = (TextView) rowView.findViewById(R.id.deal_price);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		
		txtTitle.setText(dealTitle.get(position));
		txtPrice.setText(dealPrice.get(position));
		
		Picasso.with(context).load(imageId.get(position)).placeholder(R.drawable.placeholder).into(imageView);
				
		return rowView;
	}
				
}