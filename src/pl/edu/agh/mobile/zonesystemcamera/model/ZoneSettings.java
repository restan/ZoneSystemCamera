package pl.edu.agh.mobile.zonesystemcamera.model;

import java.util.List;

import android.graphics.Bitmap;

public class ZoneSettings {
	private Bitmap preview;
	private List<CameraShotArea> areas;

	public Bitmap getPreview() {
		return preview;
	}
	public void setPreview(Bitmap preview) {
		this.preview = preview;
	}
	
	public List<CameraShotArea> getAreas() {
		return areas;
	}
	public void setAreas(List<CameraShotArea> areas) {
		this.areas = areas;
	}
}
