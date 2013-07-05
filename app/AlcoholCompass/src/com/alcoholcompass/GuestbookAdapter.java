package com.alcoholcompass;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alcoholcompass.data.GuestbookEntry;

class GuestbookAdapter extends BaseAdapter {

	private List<GuestbookEntry> mList;
	private LayoutInflater inflater;
	
	public GuestbookAdapter(Context ctx, List<GuestbookEntry> liste){
		inflater = LayoutInflater.from(ctx);
		this.mList = liste;
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = inflater.inflate(R.layout.row_guestbook, null);

		TextView nameText = (TextView) rowView
				.findViewById(R.id.textViewRowDialogName);
		TextView dateText = (TextView) rowView
				.findViewById(R.id.textViewRowDialogDate);

		GuestbookEntry entry = mList.get(position);
		Date date = new Date(entry.created * 1000);

		nameText.setText(entry.user);
		dateText.setText(date.getDay() +"." + date.getMonth() + "." + date.getYear());

		return rowView;
	}

}
