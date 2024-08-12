package com.federico.LessonBookingSystem.runners;

import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreLessonProjectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProjectionsInitializer implements CommandLineRunner {
    private final EventStoreLessonProjectionsRepository repository;

    @Autowired
    public ProjectionsInitializer(EventStoreLessonProjectionsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.printf("Starting ProjectionInitializer");
        repository.EnsureProjectionExists();

        System.out.printf("ProjectionInitializer finished");
    }
}
