package edu.ilin.watchdog.controller;

import edu.ilin.watchdog.dto.UploadFileResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/upload")
public class ImageController {


    @RequestMapping
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        return null;
    }
}
