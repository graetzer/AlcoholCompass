package com.alcoholcompass.data;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebService {
	private final static String TAG = "WebService";
	private final static String Endpoint = "http://31.172.42.83/nearby.json";
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void loadPlaces(Location loc, final PlacesHandler handler) {
		if (loc == null || handler == null) throw new InvalidParameterException("No parameter may be null");
		
		RequestParams params = new RequestParams();
		params.put("latitude", String.valueOf(loc.getLatitude()));
		params.put("longitude", String.valueOf(loc.getLongitude()));
		client.get(Endpoint, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray array) {
				ArrayList<Place> places = new ArrayList<Place>();
				try {
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Place p = new Place(
								obj.optInt("id"),
								obj.optString("name"),
								obj.optString("address"),
								obj.optInt("open_at"),
								obj.optInt("closed_at"),
								obj.optDouble("longitude"),
								obj.optDouble("latitude")
								);
						places.add(p);
					}
					
				} catch (JSONException e) {
					Log.e(TAG, "JSON Error", e);
				}
				handler.success(places);
			}
			@Override
			public void onFailure(Throwable t, String msg) {
				Log.e(TAG, msg, t);
				handler.failure();
			}
		});
	}
	
	public interface PlacesHandler {
		public void success(List<Place> places);
		public void failure();
	}
}
