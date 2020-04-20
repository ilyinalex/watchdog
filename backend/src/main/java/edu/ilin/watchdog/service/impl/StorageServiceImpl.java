package edu.ilin.watchdog.service.impl;

import edu.ilin.watchdog.exception.InternalException;
import edu.ilin.watchdog.service.StorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@PropertySource("classpath:storage.properties")
public class StorageServiceImpl implements StorageService {
    @Value("${training_dir}")
    private String trainingDir;

    private Path samplesDir;

    private ServletContext context;

    @Autowired
    public StorageServiceImpl(ServletContext context) {
        this.context = context;
    }

    @PostConstruct
    private void postConstruct() {
        this.samplesDir = Paths.get(context.getRealPath(trainingDir));
        try {
            Files.createDirectories(this.samplesDir);
        } catch (IOException e) {
            //TODO another EH
            throw new InternalException("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

        return this.samplesDir.resolve(filename).toString();
    }

    @Override
    public String store(File file, byte[] bytes) {
        try {
            FileUtils.writeByteArrayToFile(file, bytes);
        } catch (IOException e) {
            throw new InternalException("SWW with file uploading",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return file.getName();
    }
}
