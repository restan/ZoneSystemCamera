package pl.edu.agh.mobile.zonesystemcamera.model;

import android.graphics.Bitmap;

public class CameraShot {
	private Bitmap bitmap;
	private CameraShotSettings settings;

	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public CameraShotSettings getSettings() {
		return settings;
	}
	public void setSettings(CameraShotSettings settings) {
		this.settings = settings;
	}
}
