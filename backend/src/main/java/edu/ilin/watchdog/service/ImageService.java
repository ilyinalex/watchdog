package edu.ilin.watchdog.service;

import edu.ilin.watchdog.model.Image;

import java.io.File;

public interface ImageService {
    void populateSamplesDir();

    File getSamplesRoot();

    Image save(Image imag);
}
