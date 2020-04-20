package edu.ilin.watchdog.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String getSamplesDir();

    String store(MultipartFile file);
}
