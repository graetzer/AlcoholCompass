package com.alcoholcompass;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowTellFragment extends Fragment {

	Typeface cola;
	TextView title;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_showtell, container, false);
		
		cola = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cola.ttf");
		title = (TextView) view.findViewById(R.id.textViewAlcoholCompass);
		title.setTypeface(cola);
		
		
		return view;
	}

}
