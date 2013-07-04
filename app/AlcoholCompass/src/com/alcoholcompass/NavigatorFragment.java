package com.alcoholcompass;

import android.R.interpolator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class NavigatorFragment extends Fragment{
	
	private ImageView imageViewArrow;
	private Button buttonMore, buttonNavigation;
	private int lastArrowDegrees;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation, container, false);
		
		//Views, Buttons zuweisen
		imageViewArrow = (ImageView) view.findViewById(R.id.imageViewArrow);
		buttonMore = (Button) view.findViewById(R.id.buttonShowMore);
		buttonNavigation = (Button) view.findViewById(R.id.buttonNavigation);
		
		buttonMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO
			}
		});
		
		buttonNavigation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO
			}
		});
		
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
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
