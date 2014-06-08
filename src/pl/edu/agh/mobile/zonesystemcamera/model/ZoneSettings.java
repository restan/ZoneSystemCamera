package pl.edu.agh.mobile.zonesystemcamera.model;

import java.util.List;

import org.opencv.core.Mat;

public class ZoneSettings {
	private Mat preview;
	private List<CameraShotArea> areas;

	public Mat getPreview() {
		return preview;
	}
	public void setPreview(Mat preview) {
		this.preview = preview;
	}
	
	public List<CameraShotArea> getAreas() {
		return areas;
	}
	public void setAreas(List<CameraShotArea> areas) {
		this.areas = areas;
	}
}
