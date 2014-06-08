package pl.edu.agh.mobile.zonesystemcamera.model;

import java.util.List;

import org.opencv.core.Mat;

public class SceneArea {
	private int number;
	private Mat mask;
	private List<ZoneSettings> zonesSettings;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public Mat getMask() {
		return mask;
	}
	public void setMask(Mat mask) {
		this.mask = mask;
	}
	
	public List<ZoneSettings> getZonesSettings() {
		return zonesSettings;
	}
	public void setZonesSettings(List<ZoneSettings> zonesSettings) {
		this.zonesSettings = zonesSettings;
	}
}
