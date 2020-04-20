/*
 This software is the confidential information and copyrighted work of
 NetCracker Technology Corp. ("NetCracker") and/or its suppliers and
 is only distributed under the terms of a separate license agreement
 with NetCracker.
 Use of the software is governed by the terms of the license agreement.
 Any use of this software not in accordance with the license agreement
 is expressly prohibited by law, and may result in severe civil
 and criminal penalties. 
 
 Copyright (c) 1995-2020 NetCracker Technology Corp.
 
 All Rights Reserved.
 
*/
/*
 * Copyright 1995-2020 by NetCracker Technology Corp.,
 * University Office Park III
 * 95 Sawyer Road
 * Waltham, MA 02453
 * United States of America
 * All rights reserved.
 */
package edu.ilin.watchdog.service.impl;

import edu.ilin.watchdog.model.Image;
import edu.ilin.watchdog.repository.ImageRepository;
import edu.ilin.watchdog.service.ImageService;
import edu.ilin.watchdog.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

@Service
public class ImageServiceImpl implements ImageService {

    private ImageRepository imageRepository;
    private StorageService storageService;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, StorageService storageService) {
        this.imageRepository = imageRepository;
        this.storageService = storageService;
    }

    @Override
    public void populateSamplesDir() {
        throw new NotImplementedException();
    }

    @Override
    public File getSamplesRoot() {
        return new File(storageService.getSamplesDir());
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }
}
