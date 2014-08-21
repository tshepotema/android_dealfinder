package com.example.dealfinder;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapterCustom extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;

	public ExpandableListAdapterCustom(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		//final String tel = (String) getChild(groupPosition, 0);
		final String fax = (String) getChild(groupPosition, 1);
		final String email = (String) getChild(groupPosition, 2);
		final String tradHrs = (String) getChild(groupPosition, 3);
		final String address = (String) getChild(groupPosition, 4);
		//final String distance = (String) getChild(groupPosition, 5);
		//final String storeID = (String) getChild(groupPosition, 6);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.store_single, null);
		}

		TextView txtFax = (TextView) convertView.findViewById(R.id.store_fax);
		txtFax.setText(fax);

		TextView txtEmail = (TextView) convertView.findViewById(R.id.store_email);
		txtEmail.setText(email);

		TextView txtTradHrs = (TextView) convertView.findViewById(R.id.store_trade_hrs);
		txtTradHrs.setText(tradHrs);

		TextView txtAddress = (TextView) convertView.findViewById(R.id.store_address);
		txtAddress.setText(address);
						
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.stores_list, null);
		}

		TextView storeName = (TextView) convertView.findViewById(R.id.store_name);
		storeName.setText(headerTitle);

		String tel = (String) getChild(groupPosition, 0);		
		String distance = (String) getChild(groupPosition, 5);
		//String storeID = (String) getChild(groupPosition, 6);
		
		TextView tvStoreTel = (TextView) convertView.findViewById(R.id.store_tel);
		tvStoreTel.setText(tel);

		TextView tvStoreDist = (TextView) convertView.findViewById(R.id.store_distance);
		tvStoreDist.setText(distance);
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}