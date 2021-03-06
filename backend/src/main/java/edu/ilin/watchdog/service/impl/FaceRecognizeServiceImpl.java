package edu.ilin.watchdog.service.impl;

import edu.ilin.watchdog.dto.UploadFileResponse;
import edu.ilin.watchdog.exception.InternalException;
import edu.ilin.watchdog.model.Image;
import edu.ilin.watchdog.model.User;
import edu.ilin.watchdog.repository.ImageRepository;
import edu.ilin.watchdog.repository.UserRepository;
import edu.ilin.watchdog.service.FaceRecognizeService;
import edu.ilin.watchdog.service.ImageService;
import edu.ilin.watchdog.service.StorageService;
import edu.ilin.watchdog.utils.ImageUtils;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Date;
import java.util.Optional;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

@Service
public class FaceRecognizeServiceImpl implements FaceRecognizeService {

    private FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();

    private ImageService imageService;
    private StorageService storageService;

    private UserRepository userRepository;
    private ImageRepository imageRepository;


    @Autowired
    public FaceRecognizeServiceImpl(ImageService imageService, StorageServiceImpl storageService, StorageService storageService1, UserRepository userRepository, ImageRepository imageRepository, ImageUtils imageUtils) {
        this.imageService = imageService;
        this.storageService = storageService1;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        imageService.populateSamplesDir();

        train(imageService.getSamplesRoot());
    }

    private void train(File root) {

        FilenameFilter imgFilter = (dir, name) -> {
            name = name.toLowerCase();
            return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
        };

        File[] imageFiles = root.listFiles(imgFilter);

        if (imageFiles == null || imageFiles.length == 0) {
            return;
        }

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
//            System.out.println(image.getName() + " " + label);

            counter++;
        }

        faceRecognizer.train(images, labels);
    }

    private String getPredictedLabel(String imageName) {
        if (faceRecognizer.empty()) {
            return null;
        }
        Mat image = imread(imageName, IMREAD_GRAYSCALE);

        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        faceRecognizer.predict(image, label, confidence);
        int predictedLabel = label.get(0);

        return String.valueOf(predictedLabel);
    }


    @Override
    public UploadFileResponse process(MultipartFile image) {
        String imageName = storageService.store(image);

        String predictedLabel = getPredictedLabel(imageName);

        Optional<User> userOpt = Optional.empty();

        if (!StringUtils.isEmpty(predictedLabel)) {

            //TODO hell - need to decompose
            userOpt = userRepository.findById(
                    imageRepository.findById(Long.parseLong(predictedLabel))
                            .orElseThrow(() -> new InternalException("Image with id " + predictedLabel + "not found",
                                    HttpStatus.INTERNAL_SERVER_ERROR)).getUser().getId());
        }

        Image imageObj = new Image();

        try {
            imageObj.setName(image.getOriginalFilename());
            imageObj.setData(image.getBytes());
            imageObj.setCreatedAt(new Date());
            imageObj.setUpdatedAt(new Date());

            userOpt.ifPresent(imageObj::setUser);

            imageService.save(imageObj);
        }
        catch (IOException e) {
            throw new InternalException("Could not store file " + imageName + ". Please try again!", HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new UploadFileResponse();
    }
}
