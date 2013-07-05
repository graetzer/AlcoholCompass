package com.alcoholcompass;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowTellFragment extends Fragment {

	Typeface cola;
	TextView title;
	Button addKioskButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_showtell, container, false);
		
		cola = Typeface.createFromAsset(getActivity().getAssets(), "fonts/cola.ttf");
		title = (TextView) view.findViewById(R.id.textViewAlcoholCompass);
		title.setTypeface(cola);
		
		addKioskButton = (Button)view.findViewById(R.id.buttonAddKiosk);
		addKioskButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Not implemented", Toast.LENGTH_LONG).show();
			}
		});
		
		return view;
	}

}
