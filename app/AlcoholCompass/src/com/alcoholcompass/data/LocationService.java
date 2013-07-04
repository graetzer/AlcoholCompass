package com.alcoholcompass.data;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationService implements LocationListener {
	private static LocationService instance;

	public static LocationService getInstance(Context ctx) {
		if (instance == null)
			instance = new LocationService(ctx);
		return instance;
	}

	private Context mCtx;
	private final String mLocationProvider = LocationManager.GPS_PROVIDER;
	private CompassSensor compass;
	private LocationManager locationManager;

	private Location mCurrentLocation;

	private LocationService(Context ctx) {
		mCtx = ctx;

		locationManager = (LocationManager) mCtx
				.getSystemService(Context.LOCATION_SERVICE);

		mCurrentLocation = locationManager
				.getLastKnownLocation(mLocationProvider);
		compass = CompassSensor.getInstance(mCtx);
	}

	public Location currentLocation() {
		return mCurrentLocation;
	}

	public void startUpdates() {
		locationManager.requestLocationUpdates(mLocationProvider, 0, 0, this);
		compass.onResume();
	}

	public void stopUpdates() {
		locationManager.removeUpdates(this);
		compass.onPause();
	}

	public float distanceToLocation(double latitude, double longitude) {
		if (mCurrentLocation != null) {
			float[] results = new float[3];
			Location.distanceBetween(mCurrentLocation.getLatitude(),
					mCurrentLocation.getLongitude(), latitude, longitude,
					results);

			return results[0];
		}

		return 0;
	}

	public int arrowAngleTo(double latitude, double longitude) {
		if (mCurrentLocation != null) {
			
			
			float[] results = new float[3];
			Location.distanceBetween(mCurrentLocation.getLatitude(),
					mCurrentLocation.getLongitude(), latitude, longitude,
					results);
			double bearing = results[2];
			double degree = 0;

			GeomagneticField geoField = new GeomagneticField(
					Double.valueOf(mCurrentLocation.getLatitude()).floatValue(),
					Double.valueOf(mCurrentLocation.getLongitude())
							.floatValue(),
					Double.valueOf(mCurrentLocation.getAltitude()).floatValue(),
					System.currentTimeMillis());
			degree += compass.getLastDirection();
			degree += geoField.getDeclination();
			degree += bearing;// (bearing - degree) * -1;

			Log.d("LocationService", "Degree: " + degree);

			return compass.getLastDirection();//normalizeDegrees(degree);//Math.round(-degree / 360 + 180);
		}
		return 0;
	}

	public void onLocationChanged(Location location) {
		// Called when a new location is found by the network location provider.
		// makeUseOfNewLocation(location);
		if (isBetterLocation(location, mCurrentLocation))
			mCurrentLocation = location;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onProviderDisabled(String provider) {
	}

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
	
	private int normalizeDegrees(double rads){
	    return (int)((rads+360)%360);
	}

}
