package edu.ilin.watchdog.service;

import edu.ilin.watchdog.dto.UploadFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FaceRecognizeService {
    UploadFileResponse process(MultipartFile image);
}
