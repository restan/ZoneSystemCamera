package pl.edu.agh.mobile.zonesystemcamera.service;

import pl.edu.agh.mobile.zonesystemcamera.model.CameraShot;
import pl.edu.agh.mobile.zonesystemcamera.model.SceneSettings;

public interface ZoneSystemService {
	public SceneSettings prepareSceneSettings(CameraShot lightShot, CameraShot normalShot, CameraShot darkShot);
}
