package com.alcoholcompass;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alcoholcompass.data.Place;

public class PlacesListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Place> items;
	
	public PlacesListAdapter(Context ctx, List<Place> items){
		inflater = LayoutInflater.from(ctx);
		this.items = items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View rowView = inflater.inflate(R.layout.row_places, null, false);
		
		TextView name = (TextView) rowView.findViewById(R.id.textViewRowPlacesName);
		TextView distance = (TextView) rowView.findViewById(R.id.textViewRowPlacesDistance);
		
		name.setText(items.get(arg0).getName());
		distance.setText(items.get(arg0).getDistance() + "m");
		
		return rowView;
	}

}
