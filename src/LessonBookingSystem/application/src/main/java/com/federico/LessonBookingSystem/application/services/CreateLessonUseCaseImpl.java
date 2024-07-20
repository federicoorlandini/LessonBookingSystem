package com.federico.LessonBookingSystem.application.services;

import com.federico.LessonBookingSystem.application.services.ports.in.CreateLessonUseCase;
import commands.LessonCommand;
import model.Lesson;
import org.springframework.stereotype.Service;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateLessonUseCaseImpl implements CreateLessonUseCase {
    @Override
    public Lesson CreateLesson(LocalDateTime dateAndTime, int maxNumberAttenders) throws InvalidClassException {
        // Trigger the command
        var command = new LessonCommand.CreateLessonCommand(UUID.randomUUID(), dateAndTime, maxNumberAttenders);
        var newLesson = new Lesson();
        var events = newLesson.handle(command);

        // Generate the aggregate current state
        newLesson.evolve(events);

        return newLesson;
    }
}
