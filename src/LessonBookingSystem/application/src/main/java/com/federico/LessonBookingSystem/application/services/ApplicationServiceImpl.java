package com.federico.LessonBookingSystem.application.services;

import com.federico.LessonBookingSystem.application.services.ports.in.ApplicationService;
import commands.LessonCommand;
import model.Lesson;
import org.springframework.stereotype.Service;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Override
    public Lesson CreateLessonUseCase(LocalDateTime dateAndTime, int maxNumberAttenders) throws InvalidClassException {
        // Send command
        var command = new LessonCommand.CreateLessonCommand(UUID.randomUUID(), dateAndTime, maxNumberAttenders);
        var newLesson = new Lesson();
        var events = newLesson.handle(command);

        newLesson.evolve(events);

        return newLesson;
    }
}
