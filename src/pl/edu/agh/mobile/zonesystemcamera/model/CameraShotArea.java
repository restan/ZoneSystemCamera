package pl.edu.agh.mobile.zonesystemcamera.model;

import java.util.List;

public class CameraShotArea {
	private int number;
	private boolean[][] mask;
	private List<CameraShotSettings> destinationZoneSettings;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public boolean[][] getMask() {
		return mask;
	}
	public void setMask(boolean[][] mask) {
		this.mask = mask;
	}
	
	public List<CameraShotSettings> getDestinationZoneSettings() {
		return destinationZoneSettings;
	}
	public void setDestinationZoneSettings(
			List<CameraShotSettings> destinationZoneSettings) {
		this.destinationZoneSettings = destinationZoneSettings;
	}
}
