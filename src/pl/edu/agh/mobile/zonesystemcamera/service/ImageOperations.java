package pl.edu.agh.mobile.zonesystemcamera.service;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;

public class ImageOperations {

	public static Mat toMat(Bitmap bitmap) {
		Mat mat = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);
		Utils.bitmapToMat(bitmap, mat);
		return mat;
	}
	
	public static Mat getMaskWithPixelizationAndBlobsRemoval(Mat color, int minLuminance, int maxLuminance, int blockSize) {
		Mat mask = getMaskWithBlobsRemoval(color, minLuminance, maxLuminance);
		pixelizationWithBlobsRemoval(mask, blockSize);
		return mask;
	}
	
	public static Mat getMaskWithBlobsRemoval(Mat color, int minLuminance, int maxLuminance) {
		Mat mask = getMaskFromColor(color, minLuminance, maxLuminance);
		removeBlobs(mask);
		return mask;
	}
	
	public static void pixelizationWithBlobsRemoval(Mat mask, int blockSize) {
		pixelization(mask, blockSize);
		removeBlobs(mask);
	}
	
	public static Mat getMaskFromColor(Mat color, int minLuminance, int maxLuminance) {
		Mat grey = toGreyScale(color);
		return getMaskFromGray(grey, minLuminance, maxLuminance);
	}
	
	public static Mat toGreyScale(Mat rgbMat) {
		Mat greyMap = new Mat(rgbMat.rows(), rgbMat.cols(), CvType.CV_8UC1);
		Imgproc.cvtColor(greyMap, greyMap, Imgproc.COLOR_RGB2GRAY);
		return greyMap;
	}
	
	public static Mat getMaskFromGray(Mat grey, int minValue, int maxValue) {
		Mat mask = grey.clone();
		binaryThreshold(mask, minValue, maxValue);
		removeMiniBlobs(mask);
		return mask;
	}
	
	public static void binaryThreshold(Mat mask, int minValue, int maxValue) {
		Imgproc.threshold(mask, mask, minValue, 0, Imgproc.THRESH_TOZERO);
		Imgproc.threshold(mask, mask, maxValue, 0, Imgproc.THRESH_TOZERO_INV);
		Imgproc.threshold(mask, mask, 0, 255, Imgproc.THRESH_BINARY);
	}
	
	public static void removeMiniBlobs(Mat mask) {
		Imgproc.erode(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(1,1)), new Point(0,0), 1);
		Imgproc.dilate(mask, mask, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4,4)), new Point(0,0), 2);
	}
	
	public static void pixelization(Mat src, int blockSize) {
		for(int row=0; row < src.rows(); row+=blockSize) {
			for(int col=0; col < src.cols(); col+=blockSize) {
				Mat roi = src.submat(row, Math.min(row+blockSize, src.rows()), col, Math.min(col+blockSize, src.cols()));
				if (Core.mean(roi).val[0] < 150) {
					roi.setTo(new Scalar(0));
				} else {
					roi.setTo(new Scalar(255));
				}
			}
		}
	}
	
	public static void removeBlobs(Mat mask) {
		removeNegativeBlobs(mask);
		removePositiveBlobs(mask);
	}
	
	public static void removePositiveBlobs(Mat mask) {
		Core.bitwise_not(mask, mask);
		removeNegativeBlobs(mask);
		Core.bitwise_not(mask, mask);
	}
	
	public static void removeNegativeBlobs(Mat mask) {
		// Add border for proper detecting blobs at image edges
		Imgproc.copyMakeBorder(mask, mask, 2, 2, 2, 2, Imgproc.BORDER_CONSTANT, new Scalar(255));
		List<MatOfPoint> contours = getContours(mask);
		for (int i=0; i<contours.size(); i++) {
            double area = Imgproc.contourArea(contours.get(i));
            if (area < blobMaxSize(mask)) {
            	Imgproc.drawContours(mask, contours, i, new Scalar(255), -1);
            }
		}
		// Remove previously added border
		mask.submat(new Range(2, mask.rows()-2), new Range(2, mask.cols()-2)).copyTo(mask);
	}
	
	public static void xorMasksWithBlobsRemoval(List<Mat> masks) {
		xorMasks(masks);
		for (Mat mask: masks) {
			removePositiveBlobs(mask);
		}
	}
	
	public static void xorMasks(List<Mat> masks) {
		List<Mat> masksToSubtract = new ArrayList<Mat>();
		for (Mat mask: masks) {
			if (masksToSubtract.size() > 0) {
				subtract(mask, masksToSubtract);
			}
			masksToSubtract.add(mask);
		}
	}
	
	public static void subtract(Mat mask, List<Mat> masksToSubtract) {
		Mat subtractMask = Mat.zeros(mask.rows(), mask.cols(), mask.type());
		for (Mat maskToSubtract: masksToSubtract) {
			Core.bitwise_or(subtractMask, maskToSubtract, subtractMask);
		}
		Core.subtract(mask, subtractMask, mask);
	}
	
	public static List<MatOfPoint> getContours(Mat mask) {
		Mat tmpMask = mask.clone();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(tmpMask, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		return contours;
	}
	
	public static Mat getMissingMask(List<Mat> masks) {
		Mat missingMask = new Mat(masks.get(0).rows(), masks.get(0).cols(), masks.get(0).type(), new Scalar(255));
		for (Mat mask: masks) {
			Core.subtract(missingMask, mask, missingMask);
		}
		return missingMask;
	}
	
	public static int blobMaxSize(Mat image) {
		return image.rows() * image.cols() / 900;
	}

	public static Mat getPixelizedSubmask(Mat image, Mat mask, int minLuminance, int maxLuminance, int blockSize) {
		Mat grey = toGreyScale(image);
		Mat submask = Mat.zeros(mask.rows(), mask.cols(), mask.type());
		for(int row=0; row < grey.rows(); row+=blockSize) {
			for(int col=0; col < grey.cols(); col+=blockSize) {
				Mat roi = grey.submat(row, Math.min(row+blockSize, image.rows()), col, Math.min(col+blockSize, image.cols()));
				Mat maskRoi = mask.submat(row, Math.min(row+blockSize, image.rows()), col, Math.min(col+blockSize, image.cols()));
				double roiMean = Core.mean(roi).val[0];
				double maskRoiMean = Core.mean(maskRoi).val[0];
				if (maskRoiMean == 255 && roiMean >= minLuminance && roiMean < maxLuminance) {
					Mat submaskRoi = submask.submat(row, Math.min(row+blockSize, image.rows()), col, Math.min(col+blockSize, image.cols()));
					submaskRoi.setTo(new Scalar(1));
				}
			}
		}
		return submask;
	}
	
	public static Mat getEmptyImage(Mat example) {
		Mat empty = Mat.zeros(example.rows(), example.cols(), example.type());
		return empty;
	}
}
