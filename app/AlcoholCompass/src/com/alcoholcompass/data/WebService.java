package com.alcoholcompass.data;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebService {
	private final static String TAG = "WebService";
	private final static String Endpoint = "http://31.172.42.83/nearby.json";
	private final static String EndpointGuestbook = "http://31.172.42.83/guestbooks/";
	private final static String EndpointImages = "http://trash.ctdo.de/bintrash.php";
	public static AsyncHttpClient client = new AsyncHttpClient();

	public static void loadPlaces(Location loc, final PlacesHandler handler) {
		if (loc == null || handler == null)
			throw new InvalidParameterException("No parameter may be null");

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
						Place p = new Place(obj.optInt("id"), obj
								.optString("name"), obj.optString("address"),
								obj.optInt("open_at"), obj.optInt("closed_at"),
								obj.optDouble("latitude"), obj
										.optDouble("longitude"));

						JSONArray entries = obj.getJSONArray("guestbooks");
						ArrayList<GuestbookEntry> list = new ArrayList<GuestbookEntry>();
						for (int j = 0; j < entries.length(); j++) {
							JSONObject entry = entries.getJSONObject(j);
							GuestbookEntry e = new GuestbookEntry();
							e.id = entry.optInt("id");
							e.user = entry.optString("user");
							e.created = entry.optLong("created_at");
							e.imageUrl = entry.optString("url");
							list.add(e);
						}
						p.setGuestbookEntries(list);
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

	public static void sendGuestbookEntry(final Context ctx, final Place place,
			final String user, final String text, byte[] image) {

		RequestParams params = new RequestParams();
		ByteArrayInputStream input = new ByteArrayInputStream(image);
		params.put("action", "upload");
		params.put("upfile", input, "image.jpeg", "image/jpeg");
		params.put("validity", "6");

		client.post(EndpointImages, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String message) {
				String s = "Fuer Foren etc: <a href=\"";
				int start = message.indexOf(s);
				int end = message.indexOf("\">http://trash.ctdo.de/b/", start);				
				String url = "";
				if (start != -1 && end != -1)
					url = message.substring(start+s.length(), end);
				
				uploadStuff(ctx, place, user, text, url);
			}
			
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				String url = "";
				uploadStuff(ctx, place, user, text, url);
			}
		});

	}

	private static void uploadStuff(final Context ctx, final Place place,
			final String user, final String text, String url) {
		try {
			String json = "{\"guestbook\":{\"user\": \"" + user
					+ "\",\"location_id\": " + place.getID() + ", \"text\": \""
					+ text + "\", \"url\": \""+url+"\"}}";
			
			client.post(ctx, EndpointGuestbook, new StringEntity(json),
					"application/json", new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String arg1) {
							Log.i(TAG, "Success");
						}
						
						@Override
						public void onFailure(Throwable t, String msg) {
							Log.e(TAG, msg, t);
						}
					});
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Error sending guestbook entry", e);
		}
	}

	public interface PlacesHandler {
		public void success(List<Place> places);

		public void failure();
	}
}
