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

public class LocationService implements LocationListener, SensorEventListener {
	private static LocationService instance;

	private static LocationService getInstance(Context ctx) {
		if (instance == null)
			instance = new LocationService(ctx);
		return instance;
	}

	private Context mCtx;
	private final String mLocationProvider = LocationManager.GPS_PROVIDER;

	private LocationManager locationManager;
	private SensorManager sensorManager;
	private Sensor sensorAccelerometer;
	private Sensor sensorMagneticField;

	private Location mCurrentLocation;
	private double mCurrentAzimuth;

	private LocationService(Context ctx) {
		mCtx = ctx;

		sensorManager = (SensorManager) mCtx
				.getSystemService(Context.SENSOR_SERVICE);
		sensorAccelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		LocationManager locationManager = (LocationManager) mCtx
				.getSystemService(Context.LOCATION_SERVICE);

		mCurrentLocation = locationManager
				.getLastKnownLocation(mLocationProvider);
	}

	public Location currentLocation() {
		return mCurrentLocation;
	}

	public void startUpdates() {
		locationManager = (LocationManager) mCtx
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(mLocationProvider, 0, 0, this);

		sensorManager.registerListener(this, sensorAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagneticField,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void stopUpdates() {
		LocationManager locationManager = (LocationManager) mCtx
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.removeUpdates(this);
	}

	public float distanceToLocation(double latitude, long longitude) {
		if (mCurrentLocation != null) {
			float[] results = new float[3];
			Location.distanceBetween(mCurrentLocation.getLatitude(),
					mCurrentLocation.getLongitude(), latitude, longitude,
					results);

			return results[0];
		}

		return 0;
	}

	public double arrowAngleTo(double latitude, long longitude) {
		if (mCurrentLocation != null) {
			Location loc = new Location("stuff");
			loc.setLatitude(latitude);
			loc.setLongitude(longitude);
			double bearing = mCurrentLocation.bearingTo(mCurrentLocation);
			double heading = mCurrentAzimuth;

			GeomagneticField geoField = new GeomagneticField(
					Double.valueOf(mCurrentLocation.getLatitude()).floatValue(),
					Double.valueOf(mCurrentLocation.getLongitude())
							.floatValue(),
					Double.valueOf(mCurrentLocation.getAltitude()).floatValue(),
					System.currentTimeMillis());
			heading += geoField.getDeclination();
			heading = (bearing - heading) * -1;

			heading = normalizeDegree(heading);

			return heading;
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

	private double normalizeDegree(double value) {
		if (value >= 0.0f && value <= 180.0f) {
			return value;
		} else {
			return 180 + (180 + value);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] valuesAccelerometer = new float[3];
		float[] valuesMagneticField = new float[3];

		float[] matrixR = new float[9];
		float[] matrixI = new float[9];
		float[] matrixValues = new float[3];

		switch (event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				valuesAccelerometer[i] = event.values[i];
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for (int i = 0; i < 3; i++) {
				valuesMagneticField[i] = event.values[i];
			}
			break;
		}

		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
				valuesAccelerometer, valuesMagneticField);

		if (success) {
			SensorManager.getOrientation(matrixR, matrixValues);

			double azimuth = Math.toDegrees(matrixValues[0]);
			//double pitch = Math.toDegrees(matrixValues[1]);
			//double roll = Math.toDegrees(matrixValues[2]);
			
			mCurrentAzimuth = azimuth;
		}

	}
}
