package pl.edu.agh.mobile.zonesystemcamera.model;

import java.util.List;

import org.opencv.core.Mat;

public class SceneSettings {
	private Mat preview;
	private List<SceneArea> areas;

	public Mat getPreview() {
		return preview;
	}
	public void setPreview(Mat preview) {
		this.preview = preview;
	}
	
	public List<SceneArea> getAreas() {
		return areas;
	}
	public void setAreas(List<SceneArea> areas) {
		this.areas = areas;
	}
}
