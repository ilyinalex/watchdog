package edu.ilin.watchdog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface StorageService {
    String getSamplesDir();

    String store(MultipartFile file);

    String store(File file, byte[] bytes);
}
