package pl.edu.agh.mobile.zonesystemcamera.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

import pl.edu.agh.mobile.zonesystemcamera.model.CameraShot;
import pl.edu.agh.mobile.zonesystemcamera.model.CameraShotSettings;
import pl.edu.agh.mobile.zonesystemcamera.model.SceneArea;
import pl.edu.agh.mobile.zonesystemcamera.model.SceneSettings;
import pl.edu.agh.mobile.zonesystemcamera.model.ZoneSettings;

public class ZoneSystemServiceImpl implements ZoneSystemService {

	@Override
	public SceneSettings prepareSceneSettings(CameraShot lightShot, CameraShot mediumShot, CameraShot darkShot) {
		SceneSettings zoneSettings = new SceneSettings();
		List<SceneArea> areas = prepareAreas(lightShot, mediumShot, darkShot);
		zoneSettings.setAreas(areas);
		Mat preview = preparePreview(lightShot, mediumShot, darkShot, areas);
		zoneSettings.setPreview(preview);
		return zoneSettings;
	}

	private List<SceneArea> prepareAreas(CameraShot lightShot,	CameraShot mediumShot, CameraShot darkShot) {
		List<SceneArea> areas = new ArrayList<SceneArea>();
		areas.add(prepareArea(lightShot, 0, 0, 102));
		areas.add(prepareArea(lightShot, 1, 102, 154));
		areas.add(prepareArea(mediumShot, 2, 102, 154));
		areas.add(prepareArea(darkShot, 3, 102, 154));
		areas.add(prepareArea(darkShot, 4, 154, 256));
		excludeOverlapingAreas(areas);
		return areas;
	}

	private Mat preparePreview(CameraShot lightShot, CameraShot mediumShot, CameraShot darkShot, List<SceneArea> areas) {
		Mat preview = ImageOperations.getEmptyImage(mediumShot.getPhoto());
		copyFromAreas(preview, lightShot, mediumShot, darkShot, areas);
		fillMissingParts(preview, lightShot, mediumShot, darkShot, areas);
		return preview;
	}
	
	private void copyFromAreas(Mat preview, CameraShot lightShot, CameraShot mediumShot, CameraShot darkShot, List<SceneArea> areas) {
		copyFromArea(preview, lightShot, areas.get(0));
		copyFromArea(preview, lightShot, areas.get(1));
		copyFromArea(preview, mediumShot, areas.get(2));
		copyFromArea(preview, darkShot, areas.get(3));
		copyFromArea(preview, darkShot, areas.get(4));
	}
	
	private void copyFromArea(Mat preview, CameraShot cameraShot, SceneArea area) {
		cameraShot.getPhoto().copyTo(preview, area.getMask());
	}
	
	private void fillMissingParts(Mat preview, CameraShot lightShot, CameraShot mediumShot, CameraShot darkShot, List<SceneArea> areas) {
		Mat missingMask = getMissingMask(areas);
		Mat missingDark = ImageOperations.getPixelizedSubmask(mediumShot.getPhoto(), missingMask, 0, 102, getBlockSize(mediumShot.getPhoto()));
		Mat missingMid = ImageOperations.getPixelizedSubmask(mediumShot.getPhoto(), missingMask, 102, 154, getBlockSize(mediumShot.getPhoto()));
		Mat missingLight = ImageOperations.getPixelizedSubmask(mediumShot.getPhoto(), missingMask, 154, 256, getBlockSize(mediumShot.getPhoto()));

		lightShot.getPhoto().copyTo(preview, missingDark);
		mediumShot.getPhoto().copyTo(preview, missingMid);
		darkShot.getPhoto().copyTo(preview, missingLight);
	}
	
	private Mat getMissingMask(List<SceneArea> areas) {
		List<Mat> masks = new ArrayList<Mat>();
		for (SceneArea area: areas) {
			masks.add(area.getMask());
		}
		return ImageOperations.getMissingMask(masks);
	}
	
	private SceneArea prepareArea(CameraShot shot, int areaNumber, int minLuminance, int maxLuminance) {
		SceneArea sceneArea = new SceneArea();
		sceneArea.setNumber(areaNumber);
		Mat mask = ImageOperations.getMaskWithPixelizationAndBlobsRemoval(shot.getPhoto(), minLuminance, maxLuminance, getBlockSize(shot.getPhoto()));
		sceneArea.setMask(mask);
		List<ZoneSettings> zonesSettings = computeZonesSettings(shot.getSettings(), minLuminance, maxLuminance);
		sceneArea.setZonesSettings(zonesSettings);
		return sceneArea;
	}
	
	private List<ZoneSettings> computeZonesSettings(CameraShotSettings cameraShotSettings, int minLuminance, int maxLuminance) {
		int currentZone = ZoneSystem.matchBestZone(minLuminance, maxLuminance);
		return ZoneSystem.computeZonesSettings(cameraShotSettings, currentZone);
	}
	
	private void excludeOverlapingAreas(List<SceneArea> areas) {
		List<Mat> masksPriority = new ArrayList<Mat>();
		int highestPriorityIndex = areas.size()/2;
		for (int i=0; i < areas.size(); i++) {
			int multiplier = i%2 == 0 ? 1 : -1;
			int index = highestPriorityIndex + (i+1)/2 * multiplier;
			masksPriority.add(areas.get(index).getMask());
		}
		ImageOperations.xorMasksWithBlobsRemoval(masksPriority);
	}
	
	private int getBlockSize(Mat photo) {
		return Math.max(photo.rows(), photo.cols()) / 150;
	}
}
