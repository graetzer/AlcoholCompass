package com.alcoholcompass;

import java.util.ArrayList;

import android.R.interpolator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.alcoholcompass.data.Place;

public class NavigatorFragment extends Fragment{
	
	private ImageView imageViewArrow;
	private Button buttonMore, buttonNavigation;
	private ListView listViewPlaces;
	private int lastArrowDegrees;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation, container, false);
		
		//Views, Buttons zuweisen
		imageViewArrow = (ImageView) view.findViewById(R.id.imageViewArrow);
		listViewPlaces = (ListView) view.findViewById(R.id.listViewPlaces);
		buttonMore = (Button) view.findViewById(R.id.buttonShowMore);
		buttonNavigation = (Button) view.findViewById(R.id.buttonNavigation);
		
		buttonMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				LocationService service = LocationService.getInstance(getActivity());
//				double degree = service.arrowAngleTo(50.778104, 6.060867);
//				setArrow((int)degree);
				
				demoFillPlacesList();
			}
		});
		
		buttonNavigation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent navi = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:ll=" + 50.7777075 + "," + 6.0608821 ));
				startActivity(navi);
			}
		});
		
		return view;
	}
	
	private void demoFillPlacesList(){
		ArrayList<Place> liste = new ArrayList<Place>();
		liste.add(new Place("Toller Kiosk", "Adresse", 0, 0, 12, 12));
		liste.add(new Place("Noch ein besserer Kiosk", "Adresse", 0, 0, 12, 12));
		liste.add(new Place("Toller Kiosk", "Adresse", 0, 0, 12, 12));
		liste.add(new Place("Noch ein besserer Kiosk", "Adresse", 0, 0, 12, 12));
		listViewPlaces.setAdapter(new PlacesListAdapter(getActivity().getApplicationContext(), liste));
		
		Animation slide_in = AnimationUtils.loadAnimation(getActivity(), R.anim.top_down_slide);
		listViewPlaces.startAnimation(slide_in);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LocationService service = LocationService.getInstance(getActivity());
		service.startUpdates();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		LocationService service = LocationService.getInstance(getActivity());
		service.stopUpdates();
	}
	
	private void init(){
		lastArrowDegrees = 0;
	}
	
	private void setArrow(int degrees){
		
		degrees = degrees % 360;
		final int newDegrees = degrees;
		if(degrees == lastArrowDegrees) return;
		
		imageViewArrow.setRotation(0);
		
		//Animation erstellen
		RotateAnimation animation = new RotateAnimation(lastArrowDegrees, newDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillEnabled(true);
		animation.setFillBefore(true);
		animation.setDuration(1000);
		animation.setInterpolator(getActivity().getApplicationContext(), interpolator.accelerate_decelerate);
		
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				lastArrowDegrees = newDegrees;
				imageViewArrow.setRotation(newDegrees);
			}
		});
		
		imageViewArrow.startAnimation(animation);
		
	}
	
}
