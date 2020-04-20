package edu.ilin.watchdog.controller;

import edu.ilin.watchdog.dto.UploadFileResponse;
import edu.ilin.watchdog.service.FaceRecognizeService;
import edu.ilin.watchdog.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/upload")
public class ImageController {

    private FaceRecognizeService faceRecognizeService;

    @Autowired
    public ImageController(FaceRecognizeService faceRecognizeService) {
        this.faceRecognizeService = faceRecognizeService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        return faceRecognizeService.process(file);
    }
}
