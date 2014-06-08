package pl.edu.agh.mobile.zonesystemcamera.model;

public class ZoneSettings {
	private int number;
	private CameraShotSettings captureSettings;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public CameraShotSettings getCaptureSettings() {
		return captureSettings;
	}
	public void setCaptureSettings(CameraShotSettings captureSettings) {
		this.captureSettings = captureSettings;
	}
	
}
