package edu.ilin.watchdog.service.impl;

import edu.ilin.watchdog.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {
    @Value("${training_dir}")
    private String trainingDir;

    @Override
    public String getSamplesDir() {
        return trainingDir;
    }
}
