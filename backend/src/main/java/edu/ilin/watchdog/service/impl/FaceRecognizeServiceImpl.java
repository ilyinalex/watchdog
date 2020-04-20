package edu.ilin.watchdog.service.impl;

import edu.ilin.watchdog.service.FaceRecognizeService;
import edu.ilin.watchdog.service.ImageService;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

@Service
public class FaceRecognizeServiceImpl implements FaceRecognizeService {

    @Value("${training_dir}")
    private String trainingDir;

    private FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();

    private ImageService imageService;

    @Autowired
    public FaceRecognizeServiceImpl(ImageService imageService) {
        this.imageService = imageService;
        imageService.populateSamplesDir();

        train();
    }

    private void train() {
        File root = new File(trainingDir);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);

        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), IMREAD_GRAYSCALE);

            images.put(counter, img);
            String label = "-1";

            try {
                label = image.getName().split("\\.")[0];

                labelsBuf.put(counter, Integer.parseInt(label));
            } catch (Exception e) {
                labelsBuf.put(counter, -1);
            }
            System.out.println(image.getName() + " " + label);

            counter++;
        }

        faceRecognizer.train(images, labels);
    }
}
