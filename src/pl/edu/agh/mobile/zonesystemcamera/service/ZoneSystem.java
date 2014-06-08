package pl.edu.agh.mobile.zonesystemcamera.service;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.mobile.zonesystemcamera.model.CameraShotSettings;
import pl.edu.agh.mobile.zonesystemcamera.model.ZoneSettings;

public class ZoneSystem {
	public static double SHUTTER_SPEED_STEP = 2;
	public static double APERTURE_STEP = Math.sqrt(2.0);
	
	public static List<ZoneSettings> computeZonesSettings(CameraShotSettings cameraShotSettings, int currentZone) {
		List<ZoneSettings> zoneSettings = new ArrayList<ZoneSettings>();
		for (int zone=1; zone < 10; zone++) {
			zoneSettings.add(computeZoneCaptureSettings(zone, cameraShotSettings, currentZone));
		}
		return zoneSettings;
	}
	
	public static ZoneSettings computeZoneCaptureSettings(int zone, CameraShotSettings baseSettings, int baseZone) {
		int zoneDifference = Math.abs(baseZone-zone);
		int apertureSteps = zoneDifference/2;
		int shutterSteps = zoneDifference/2 + zoneDifference%2;
		double apertureMultiplier = 1.0 / Math.pow(APERTURE_STEP, apertureSteps);
		double shutterMultiplier = Math.pow(SHUTTER_SPEED_STEP, shutterSteps);
		if (zone < baseZone) {
			apertureMultiplier = 1.0 / apertureMultiplier;
			shutterMultiplier = 1.0 / shutterMultiplier;
		}
		CameraShotSettings zoneCaptureSettings = new CameraShotSettings();
		zoneCaptureSettings.setAperture(baseSettings.getAperture() * apertureMultiplier);
		zoneCaptureSettings.setShutterSpeed(baseSettings.getShutterSpeed() * shutterMultiplier);
		ZoneSettings zoneSettings = new ZoneSettings();
		zoneSettings.setNumber(zone);
		zoneSettings.setCaptureSettings(zoneCaptureSettings);
		return zoneSettings;
	}
	
	public static int matchBestZone(double minLuminance, double maxLuminance) {
		double mean = (minLuminance + maxLuminance) / 2;
		return matchZone(mean);
	}
	
	public static int matchZone(double luminance) {
		// from http://dpanswers.com/content/tech_zonesystem.php
		if (luminance < 15.5) {
			return 1;
		} else if (luminance < 43) {
			return 2;
		} else if (luminance < 70.5) {
			return 3;
		} else if (luminance < 106) {
			return 4;
		} else if (luminance < 148) {
			return 5;
		} else if (luminance < 191) {
			return 6;
		} else if (luminance < 228) {
			return 7;
		} else if (luminance < 249.5) {
			return 8;
		} else {
			return 9;
		}
	}
}
