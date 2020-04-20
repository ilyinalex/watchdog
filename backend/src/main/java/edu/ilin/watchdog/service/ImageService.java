package edu.ilin.watchdog.service;

import java.io.File;

public interface ImageService {
    void populateSamplesDir();

    File getSamplesRoot();
}
