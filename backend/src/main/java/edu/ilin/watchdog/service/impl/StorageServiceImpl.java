package edu.ilin.watchdog.service.impl;

import edu.ilin.watchdog.exception.InternalException;
import edu.ilin.watchdog.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceImpl implements StorageService {
    @Value("${training_dir}")
    private String trainingDir;

    private final Path samplesDir;

    @Autowired
    public StorageServiceImpl() {
        this.samplesDir = Paths.get(trainingDir);
    }

    @Override
    public String getSamplesDir() {
        return trainingDir;
    }

    @Override
    public String store(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new InternalException("Failed to store empty file " + filename,
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (filename.contains("..")) {

                throw new InternalException(
                        "Cannot store file with relative path outside current directory "
                                + filename, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.samplesDir.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new InternalException("Failed to store file " + filename, e);
        }

        return filename;
    }
}
