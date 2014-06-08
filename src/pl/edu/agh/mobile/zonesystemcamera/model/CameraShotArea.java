package pl.edu.agh.mobile.zonesystemcamera.model;

import java.util.List;

import org.opencv.core.Mat;

public class CameraShotArea {
	private int number;
	private Mat mask;
	private List<CameraShotSettings> destinationZoneSettings;
	
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
	
	public List<CameraShotSettings> getDestinationZoneSettings() {
		return destinationZoneSettings;
	}
	public void setDestinationZoneSettings(List<CameraShotSettings> destinationZoneSettings) {
		this.destinationZoneSettings = destinationZoneSettings;
	}
}
