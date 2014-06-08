package pl.edu.agh.mobile.zonesystemcamera.model;

import org.opencv.core.Mat;

public class CameraShot {
	private Mat photo;
	private CameraShotSettings settings;

	public Mat getPhoto() {
		return photo;
	}
	public void setPhoto(Mat photo) {
		this.photo = photo;
	}

	public CameraShotSettings getSettings() {
		return settings;
	}
	public void setSettings(CameraShotSettings settings) {
		this.settings = settings;
	}
}
