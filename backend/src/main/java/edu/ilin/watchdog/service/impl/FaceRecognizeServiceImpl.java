package edu.ilin.watchdog.service.impl;

import edu.ilin.watchdog.service.FaceRecognizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class FaceRecognizeServiceImpl implements FaceRecognizeService {

    @Value("${training_dir}")
    private String trainingDir;

    @Autowired
    public FaceRecognizeServiceImpl() {

    }

    private void train() {
        throw new NotImplementedException();
    }
}
