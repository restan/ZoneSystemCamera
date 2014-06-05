package pl.edu.agh.mobile.zonesystemcamera.service;

import pl.edu.agh.mobile.zonesystemcamera.model.CameraShot;
import pl.edu.agh.mobile.zonesystemcamera.model.ZoneSettings;

public interface ZoneSystemService {
	public ZoneSettings prepareZoneSettings(CameraShot lightShot, CameraShot normalShot, CameraShot darkShot);
}
